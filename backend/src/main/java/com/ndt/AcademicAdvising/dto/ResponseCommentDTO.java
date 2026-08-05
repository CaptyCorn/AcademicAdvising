/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.dto;

import java.util.Date;
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
public class ResponseCommentDTO {
    private int id;
    private String content;
    private Date createdAt;
    private String nameUserComment;
    private int postId;
}
