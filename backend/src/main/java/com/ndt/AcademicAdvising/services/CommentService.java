/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseCommentDTO;
import com.ndt.AcademicAdvising.pojo.Comment;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface CommentService {
    Comment getComment(int postId);
    PageResponseDTO<ResponseCommentDTO> getComments (Map<String, String> params, int postId);
    ResponseCommentDTO addComment(String content, int postId);
    void addCommentAI(String content, int postId);
    ResponseCommentDTO updateComment(int commentId, String content);
    void deleteComment(int commentId);
}
