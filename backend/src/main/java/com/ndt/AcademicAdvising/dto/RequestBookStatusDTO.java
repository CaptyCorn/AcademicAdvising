/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import com.ndt.AcademicAdvising.enums.BookStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class RequestBookStatusDTO {
    @NotNull(message = "Trạng thái sách không được để trống.")
    private BookStatus status;
}
