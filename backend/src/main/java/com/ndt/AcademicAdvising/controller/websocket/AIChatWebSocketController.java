/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.websocket;

import com.ndt.AcademicAdvising.dto.RequestAIChatDTO;
import com.ndt.AcademicAdvising.dto.ResponseMessageDTO;
import com.ndt.AcademicAdvising.services.impl.AIChatService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author ngodo
 */
@Controller
public class AIChatWebSocketController {
    @Autowired
    private AIChatService aiChatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ai/chat")
    public void chat(RequestAIChatDTO request, Principal principal) {

        ResponseMessageDTO response = aiChatService.chat(request);

        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/ai",
                response
        );
    }
}
