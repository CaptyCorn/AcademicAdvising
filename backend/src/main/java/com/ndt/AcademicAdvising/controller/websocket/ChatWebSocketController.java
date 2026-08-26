/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.websocket;

import com.ndt.AcademicAdvising.dto.RequestMessageDTO;
import com.ndt.AcademicAdvising.dto.ResponseMessageDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.services.MessageService;
import com.ndt.AcademicAdvising.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author ngodo
 */
@Controller
public class ChatWebSocketController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/chat")
    void sendMessage(RequestMessageDTO data) {
        ResponseMessageDTO responseMessage = this.messageService.createMessage(data);
        ResponseUserDTO receiver = this.userService.getUserById(data.getReceiverId());
        this.simpMessagingTemplate.convertAndSendToUser(
                receiver.getUsername(), 
                "/queue/messages", 
                responseMessage
        );
    }
}
