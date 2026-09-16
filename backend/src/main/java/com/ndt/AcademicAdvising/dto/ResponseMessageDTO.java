/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import com.ndt.AcademicAdvising.enums.MessageType;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseMessageDTO {
    private Integer id;
    private String content;
    private Date createdAt;
    private Integer conversationId;
    private MessageType messageType;
    private ResponseUserDTO sender;
    private ResponseBookDTO book;
}
