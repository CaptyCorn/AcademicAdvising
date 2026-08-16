/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.RequestChatDTO;
import com.ndt.AcademicAdvising.rag.RAGAssistant;
import com.ndt.AcademicAdvising.services.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class AIServiceImpl implements AIService{
    @Autowired
    private RAGAssistant ragService;

    @Override
    public String getMessage(RequestChatDTO request) {
        return ragService.chat(request.getUserId(), request.getQuestion());
    }
}
