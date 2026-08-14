/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import com.ndt.AcademicAdvising.enums.BookCondition;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseBookDetailDTO {
    private int id;
    private String name;
    private String description;
    private double price;
    private BookCondition condition;
    private Set<ResponseSubjectBookDTO> subjects;
    private Set<ResponseBookIImgDTO> images;
}
