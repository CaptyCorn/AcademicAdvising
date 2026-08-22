/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 *
 * @author ngodo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObjectDTO {
    private Boolean status;
    private int statusCode;
    private String message;
    private Object data;
}
