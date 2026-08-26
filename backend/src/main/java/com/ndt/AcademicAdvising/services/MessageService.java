/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.RequestMessageDTO;
import com.ndt.AcademicAdvising.dto.ResponseMessageDTO;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface MessageService {
    ResponseMessageDTO createMessage(RequestMessageDTO data);
    PageResponseDTO<ResponseMessageDTO> getListMessage(Map<String, String> param, int conversationId);
}
