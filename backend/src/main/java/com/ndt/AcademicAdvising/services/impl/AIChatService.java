/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.RequestAIChatDTO;
import com.ndt.AcademicAdvising.dto.ResponseMessageDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.enums.UserRole;
import com.ndt.AcademicAdvising.pojo.Conversation;
import com.ndt.AcademicAdvising.pojo.Message;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.ConversationRepository;
import com.ndt.AcademicAdvising.repositories.MessageRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class AIChatService {
    @Autowired
    private AIService aiService;

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private UserRepository userRepo;
    
    private ResponseUserDTO toUserDTO(User u) {
        ResponseUserDTO dto = new ResponseUserDTO();
        dto.setName(u.getName());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setStudentCode(u.getStudentCode());
        dto.setAvatar(u.getAvatar());
        
        return dto;
    }
    
    private ResponseMessageDTO toDTO(Message message) {
        ResponseMessageDTO dto = new ResponseMessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setConversationId(message.getConversation().getId());
        dto.setSender(toUserDTO(message.getSender()));
        dto.setCreatedAt(message.getCreatedAt());
        
        return dto;
    }
    
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(username);
    }
    
    public ResponseMessageDTO chat(RequestAIChatDTO request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("Nội dung không được để trống.");
        }
        
        User currentUser = getCurrentUser();
        User ai = this.userRepo.findByUserRole(UserRole.ROLE_AI);
        Conversation conversation;

        if (request.getConversationId() != null) {

            conversation = this.conversationRepo.findById(request.getConversationId())
                    .orElseThrow(() -> new IllegalArgumentException("Conversation không tồn tại"));
            
            boolean isParticipant = 
                    conversation.getSender().getId().equals(currentUser.getId()) || 
                    conversation.getReceiver().getId().equals(currentUser.getId());

            if (!isParticipant) {
                throw new IllegalArgumentException("Bạn không có quyền truy cập conversation này.");
            }
        } else {          
            conversation = conversationRepo.findConversationBetweenUsers(
                                    currentUser.getId(),
                                    ai.getId()
            ).orElseGet(() -> { 
                Conversation c = new Conversation();
                c.setSender(currentUser);
                c.setReceiver(ai);

                return conversationRepo.save(c);
            });
        }
        
        Message userMessage = new Message();
        userMessage.setContent(request.getContent());
        userMessage.setSender(currentUser);
        userMessage.setConversation(conversation);
        
        Message savedUserMessage = this.messageRepo.save(userMessage);

        conversation.setLastMessage(savedUserMessage.getContent());
        conversation.setLastSender(currentUser);

        this.conversationRepo.save(conversation);
        
        String answer = aiService.chat(conversation.getId(), request.getContent());

        Message aiMessage = new Message();
        aiMessage.setContent(answer);
        aiMessage.setSender(ai);
        aiMessage.setConversation(conversation);
        
        Message savedAIMessage = messageRepo.save(aiMessage);


        conversation.setLastMessage(answer);
        conversation.setLastSender(ai);

        this.conversationRepo.save(conversation);

        return toDTO(savedAIMessage);
    }
}
