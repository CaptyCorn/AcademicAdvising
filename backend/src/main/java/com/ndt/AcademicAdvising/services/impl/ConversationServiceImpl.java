/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseConversationDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.pojo.Conversation;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.ConversationRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.ConversationService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class ConversationServiceImpl implements ConversationService{
    
    @Autowired
    private ConversationRepository conversationRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    private User getCurrentUser() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(currentUser);
    }
    
    private ResponseUserDTO toUserDTO(User u) {
        ResponseUserDTO dto = new ResponseUserDTO();
        dto.setName(u.getName());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setStudentCode(u.getStudentCode());
        dto.setAvatar(u.getAvatar());
        
        return dto;
    }
    
    private ResponseConversationDTO toDTO(Conversation conversation) {
        ResponseConversationDTO dto = new ResponseConversationDTO();
        dto.setId(conversation.getId());
        dto.setLastSender(toUserDTO(conversation.getLastSender()));
        dto.setLastMessage(conversation.getLastMessage());
        dto.setLastMessageTime(conversation.getLastMessageTime());
        dto.setSender(toUserDTO(conversation.getSender()));
        dto.setReceiver(toUserDTO(conversation.getReceiver()));
        dto.setCreatedAt(conversation.getCreatedAt());
        
        return dto;
    }
    
    private PageResponseDTO<ResponseConversationDTO> toPageDTO(Page<ResponseConversationDTO> page) {
        PageResponseDTO<ResponseConversationDTO> dto = new PageResponseDTO<>();
        dto.setContent(page.getContent());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }

    @Override
    public PageResponseDTO<ResponseConversationDTO> getListConversation(Map<String, String> params) {
        return toPageDTO(this.conversationRepo.getListConversation(params, getCurrentUser().getId()).map(this::toDTO));
    }
    
}
