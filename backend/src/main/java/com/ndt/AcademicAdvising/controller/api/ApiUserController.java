/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.RequestUpdateUserDTO;
import com.ndt.AcademicAdvising.dto.RequestUserLoginDTO;
import com.ndt.AcademicAdvising.dto.RequestUserRegisterDTO;
import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.AuthService;
import com.ndt.AcademicAdvising.services.UserService;
import com.ndt.AcademicAdvising.utils.JwtUtils;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    ResponseEntity<ResponseObjectDTO> login(@RequestBody RequestUserLoginDTO user) {
        if (this.authService.verify(user)) {
            try {
                String token = this.jwtUtils.generateToken(user.getUsername());
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(
                                new ResponseObjectDTO(
                                        Boolean.TRUE,
                                        200,
                                        "Đăng nhập thành công",
                                        token)
                        );
            } catch (Exception ex) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                new ResponseObjectDTO(
                                        Boolean.FALSE,
                                        500,
                                        "Lỗi tạo JWT",
                                        null)
                        );
            }
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ResponseObjectDTO(
                                Boolean.FALSE,
                                401,
                                "Sai thông tin đăng nhập",
                                null)
                );

    }

    @PostMapping("/register")
    ResponseEntity<ResponseObjectDTO> register(@Valid @ModelAttribute RequestUserRegisterDTO userDTO) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    201,
                                    "Đăng ký thành công",
                                    this.userService.addUser(userDTO))
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

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ResponseObjectDTO> getProfile(Principal principal) {
        try {
            String username = principal.getName();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Lấy thông tin người dùng thành công",
                                    this.userService.getProfile(username))
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    401,
                                    i.getMessage(),
                                    null)
                    );
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ResponseObjectDTO> update(@Valid @ModelAttribute RequestUpdateUserDTO userDTO,
            Principal principal) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Cập nhập thông tin thành công",
                                    this.userService.updateUser(principal.getName(), userDTO))
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    e.getMessage(),
                                    null)
                    );
        }
    }
}
