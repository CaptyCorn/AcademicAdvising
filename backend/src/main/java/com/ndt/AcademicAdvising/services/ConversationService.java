/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseConversationDTO;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface ConversationService {
    PageResponseDTO<ResponseConversationDTO> getListConversation(Map<String, String> params);
}
