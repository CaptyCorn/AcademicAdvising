/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.NotificationService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/ouacademic")
public class ApiNotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping("/notifications")
    ResponseEntity<ResponseObjectDTO> list(@RequestParam Map<String, String> param) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Lấy danh sách tin nhắn thành công",
                                    this.notificationService.getListNotification(param))
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }
}
