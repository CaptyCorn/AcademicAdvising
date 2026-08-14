/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import com.ndt.AcademicAdvising.enums.BookCondition;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Data
public class RequestBookDTO {
    @NotBlank(message = "Tên sách không được để trống.")
    private String name;
    
    @NotBlank(message = "Mô tả sách không được để trống.")
    private String description;
    
    @NotBlank(message = "Giá không được để trống.")
    private double price;
    
    @NotBlank(message = "Tình trạng sách không được để trống.")
    private BookCondition condition;
    
    private List<Integer> subjectIds;
    private List<MultipartFile> files;
}
