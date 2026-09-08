/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.RequestMessageDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookIImgDTO;
import com.ndt.AcademicAdvising.dto.ResponseMessageDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.enums.MessageType;
import com.ndt.AcademicAdvising.pojo.Book;
import com.ndt.AcademicAdvising.pojo.BookImage;
import com.ndt.AcademicAdvising.pojo.Conversation;
import com.ndt.AcademicAdvising.pojo.Message;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.ConversationRepository;
import com.ndt.AcademicAdvising.repositories.MessageRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.MessageService;
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
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private UserRepository userRepo;

    private ResponseUserDTO toUserDTO(User u) {
        ResponseUserDTO dto = new ResponseUserDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setStudentCode(u.getStudentCode());
        dto.setAvatar(u.getAvatar());

        return dto;
    }
    
    private ResponseBookIImgDTO toDTOImg(BookImage bookImg) {
        ResponseBookIImgDTO dto = new ResponseBookIImgDTO();
        dto.setId(bookImg.getId());
        dto.setImageUrl(bookImg.getImageUrl());
        dto.setCreatedAt(bookImg.getCreatedAt());
        return dto;
    }

    private ResponseMessageDTO toDTO(Message message) {
        ResponseMessageDTO dto = new ResponseMessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setConversationId(message.getConversation().getId());
        dto.setSender(toUserDTO(message.getSender()));
        dto.setCreatedAt(message.getCreatedAt());
        dto.setMessageType(message.getMessageType());

        ResponseUserDTO sender = new ResponseUserDTO();
        sender.setId(message.getSender().getId());
        sender.setUsername(message.getSender().getUsername());
        sender.setName(message.getSender().getName());

        dto.setSender(sender);

        if (message.getMessageType() == MessageType.BOOK
                && message.getBook() != null) {

            Book book = message.getBook();

            ResponseBookDTO bookDTO = new ResponseBookDTO();

            bookDTO.setId(book.getId());
            bookDTO.setName(book.getName());
            bookDTO.setPrice(book.getPrice());

            // Lấy ảnh đầu tiên
            if (book.getBookImages() != null) {
                bookDTO.setImage(
                        book.getBookImages()
                                .stream()
                                .findFirst()
                                .map(this::toDTOImg)
                                .orElse(null)
                );
            }

            dto.setBook(bookDTO);
        }

        return dto;
    }

    private PageResponseDTO<ResponseMessageDTO> toPageDTO(Page<ResponseMessageDTO> page) {
        PageResponseDTO<ResponseMessageDTO> dto = new PageResponseDTO<>();
        dto.setContent(page.getContent());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }

    private User getCurrentUser() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(currentUser);
    }

    @Override
    public ResponseMessageDTO createMessage(RequestMessageDTO data) {
        String content = data.getContent();
        Integer receiverId = data.getReceiverId();
        Integer conversationIdInte = data.getConversationId();

        if (content.isBlank()) {
            throw new IllegalArgumentException("Nội dung không được trống");
        }

        User currentUser = getCurrentUser();

        Conversation conversation;
        if (conversationIdInte != null) {
            int conversationId = conversationIdInte;
            conversation = this.conversationRepo.findById(conversationId)
                    .orElseThrow();
        } else {
            User receiver = userRepo.findById(receiverId)
                    .orElseThrow(()
                            -> new IllegalArgumentException(
                            "Người nhận không tồn tại"
                    )
                    );

            conversation = this.conversationRepo.findConversationBetweenUsers(
                    currentUser.getId(), receiver.getId())
                    .orElseGet(() -> {
                        Conversation newConversation = new Conversation();
                        newConversation.setSender(currentUser);
                        newConversation.setReceiver(receiver);

                        return newConversation;
                    });

            conversation.setSender(getCurrentUser());
            conversation.setReceiver(this.userRepo.findById(receiverId).orElseThrow());
        }
        conversation.setLastMessage(content);
        conversation.setLastSender(currentUser);

        Message message = new Message();
        message.setContent(content);
        message.setSender(currentUser);
        message.setMessageType(MessageType.TEXT);
        message.setConversation(conversation);

        this.conversationRepo.save(conversation);
        return toDTO(this.messageRepo.save(message));
    }

    @Override
    public PageResponseDTO<ResponseMessageDTO> getListMessage(Map<String, String> param, int conversationId) {
        return toPageDTO(this.messageRepo.getListMessage(param, conversationId).map(this::toDTO));
    }

}
