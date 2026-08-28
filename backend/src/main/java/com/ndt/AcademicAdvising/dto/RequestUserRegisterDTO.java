/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserRegisterDTO {
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 15 kí tự")
    private String firstName;
    
    @NotBlank(message = "Họ không được để trống")
    @Size(min = 2, max = 50, message = "Họ phải từ 2 đến 15 kí tự")
    private String lastName;
    
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 8, max = 50, message = "Tên đăng nhập phải từ 6 đến 50 kí tự")
    private String username;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt"
    )
    private String password;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không dúng định dạng")
    private String email;
    
    @NotBlank(message = "Mã số sinh viên không được để trống")
    @Size(min = 10, max = 15, message = "Mã số sinh viên phải chứa ít nhất 10 kí tự")
    private String studentCode;
}
