/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author ngodo
 */
@Data
public class ResponseBookIImgDTO {
    private int id;
    private String imageUrl;
    private Date createdAt;
}
