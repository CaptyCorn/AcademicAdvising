/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.RequestChatDTO;
import com.ndt.AcademicAdvising.dto.ResponseChatDTO;
import com.ndt.AcademicAdvising.services.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/ouacademic")
public class ApiAIController {
    
    @Autowired
    private AIService aiSevice;
    
    @PostMapping("/question")
    public ResponseChatDTO getMessageRequest(@RequestBody RequestChatDTO dto) {
        return new ResponseChatDTO(this.aiSevice.getMessage(dto));
    }
}
