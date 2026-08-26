/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseMessageDTO;
import com.ndt.AcademicAdvising.pojo.Message;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface MessageService {
    ResponseMessageDTO createMessage(Map<String, String> data);
    PageResponseDTO<ResponseMessageDTO> getListMessage(Map<String, String> param, int conversationId);
}
