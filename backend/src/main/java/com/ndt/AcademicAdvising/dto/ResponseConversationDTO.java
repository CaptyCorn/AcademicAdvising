/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseConversationDTO {
    private int id;
    private String lastMessage;
    private Date lastMessageTime;
    private ResponseUserDTO lastSender;
    private ResponseUserDTO sender;
    private ResponseUserDTO receiver;
    private Date createdAt;
}
