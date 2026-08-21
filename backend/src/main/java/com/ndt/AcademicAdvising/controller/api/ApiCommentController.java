/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.RequestCommentDTO;
import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/ouacademic")
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    ResponseEntity<ResponseObjectDTO> list(@PathVariable(value = "postId") int postId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Lấy danh sách comment thành công",
                                    this.commentService.getComments(postId)
                            ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }

    @PostMapping("/posts/{postId}/comment")
    ResponseEntity<ResponseObjectDTO> insert(@PathVariable(value = "postId") int postId,
            @RequestBody RequestCommentDTO comment) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    201,
                                    "Thêm bình luận thành công",
                                    this.commentService.addComment(comment.getContent(), postId)
                            ));
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    ResponseEntity<ResponseObjectDTO> update(@PathVariable(value = "commentId") int commentId,
            @RequestBody RequestCommentDTO comment) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Cập nhật bình luận thành công",
                                    this.commentService.updateComment(commentId, comment.getContent())
                            ));
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    ResponseEntity<ResponseObjectDTO> delete(@PathVariable(value = "commentId") int commentId) {
        try {
            this.commentService.deleteComment(commentId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Xoá bình luận thành công",
                                    null)
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }
}
