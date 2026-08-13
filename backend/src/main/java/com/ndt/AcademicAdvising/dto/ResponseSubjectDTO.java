/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseSubjectDTO {
    private int id;
    private String name;
    private String description;
    private int majorId;
}
