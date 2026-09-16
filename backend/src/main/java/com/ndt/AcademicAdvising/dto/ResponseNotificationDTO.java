/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import com.ndt.AcademicAdvising.enums.NotificationType;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseNotificationDTO {
    private Integer id;
    private String title;
    private String content;
    private NotificationType type;
    private Boolean isRead;
    private String link;
    private Date createdAt;
}
