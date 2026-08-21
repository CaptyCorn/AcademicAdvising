/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Data
public class RequestUpdateUserDTO {   
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 15, message = "Tên phải từ 2 đến 15 kí tự")
    private String firstName;
    
    @NotBlank(message = "Họ không được để trống")
    @Size(min = 2, max = 15, message = "Họ phải từ 2 đến 15 kí tự")
    private String lastName;
    
    
    @Size(min = 10, max = 11, message = "Mã số sinh viên phải từ 10 đến 11 kí tự")
    private String phone;
    
    private MultipartFile file;
}
