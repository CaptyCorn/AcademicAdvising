/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import com.ndt.AcademicAdvising.enums.BookCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Data
public class RequestBookDTO {
    @NotBlank(message = "Chưa nhập tên sách.")
    private String name;
    
    @NotBlank(message = "Mô tả sách không được để trống.")
    private String description;
    
    @NotNull(message = "Chưa nhập giá sách.")
    private Double price;
    
    private BookCondition condition;
    
    private List<Integer> subjectIds;
    private List<MultipartFile> files;
}
