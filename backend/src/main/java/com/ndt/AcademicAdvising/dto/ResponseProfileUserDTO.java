/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ngodo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProfileUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String studentCode;
    private String phone;
    private String avatar;
}
