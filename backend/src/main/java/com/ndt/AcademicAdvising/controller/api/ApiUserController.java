/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.RequestUserLoginDTO;
import com.ndt.AcademicAdvising.services.AuthService;
import com.ndt.AcademicAdvising.services.UserService;
import com.ndt.AcademicAdvising.utils.JwtUtils;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@Controller
@RestController
@RequestMapping("/ouacademic")
public class ApiUserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody RequestUserLoginDTO user) {
        if (this.authService.verify(user)) {
            try {
                String token = this.jwtUtils.generateToken(user.getUsername());
                return ResponseEntity.ok()
                        .body(Collections.singletonMap("token", token));
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Sai thông tin đăng nhập");

    }
}
