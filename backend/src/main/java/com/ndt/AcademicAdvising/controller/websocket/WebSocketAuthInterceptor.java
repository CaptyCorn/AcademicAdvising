/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.websocket;

import com.ndt.AcademicAdvising.services.UserService;
import com.ndt.AcademicAdvising.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author ngodo
 */
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor{
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    private static final String AUTHENTICATION_SESSION_ATTRIBUTE = "wsAuthentication";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message,
                StompHeaderAccessor.class
        );

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing Authorization");
            }

            String token = authHeader.substring(7);

            try {
                String username = jwtUtils.extractUserName(token);

                UserDetails user = userService.loadUserByUsername(username);

                if (!jwtUtils.validateToken(token, user)) {
                    throw new IllegalArgumentException("Invalid JWT");
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                accessor.setUser(authentication);
                if (accessor.getSessionAttributes() != null) {
                    accessor.getSessionAttributes().put(AUTHENTICATION_SESSION_ATTRIBUTE, authentication);
                }

            } catch (JwtException | IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid JWT", ex);
            }
        } else if (accessor.getUser() == null && accessor.getSessionAttributes() != null) {
            Object sessionAuthentication = accessor.getSessionAttributes().get(AUTHENTICATION_SESSION_ATTRIBUTE);
            if (sessionAuthentication instanceof Authentication authentication) {
                accessor.setUser(authentication);
            }
        }

        return message;
    }
}
