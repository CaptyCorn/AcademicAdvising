/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseNotificationDTO;
import com.ndt.AcademicAdvising.pojo.User;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface NotificationService {
    ResponseNotificationDTO createCommentNotification(User sender, User receiver, int postId);
    PageResponseDTO<ResponseNotificationDTO> getListNotification(Map<String, String> param);
}
