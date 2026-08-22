/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.async.AIPostAsyncService;
import com.ndt.AcademicAdvising.dto.RequestPostDTO;
import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import com.ndt.AcademicAdvising.services.PostService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class ApiPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AIPostAsyncService postAsyncService;

    @GetMapping("/posts")
    ResponseEntity<ResponseObjectDTO> getAllPost() {

        Page<ResponsePostDTO> data = this.postService.getListPost();
        if (!data.getContent().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Lấy danh sách bài đăng thành công",
                                    data)
                    );
        }
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

    @PostMapping("/post")
    ResponseEntity<ResponseObjectDTO> insertPost(@RequestBody Map<String, String> p) {
        try {
            ResponsePostDTO postDTO = this.postService.addPost(p.get("content"));

            this.postAsyncService.processPost(postDTO.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    201,
                                    "Tạo bài đăng thành công",
                                    postDTO)
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

    @PutMapping("/posts/{postId}")
    ResponseEntity<ResponseObjectDTO> updatePost(@PathVariable(value = "postId") int postId,
            @RequestBody RequestPostDTO p) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Cập nhật bài đăng thành công.",
                                    this.postService.updatePost(postId, p.getContent()))
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

    @DeleteMapping("/posts/{postId}")
    ResponseEntity<ResponseObjectDTO> deletePost(@PathVariable(value = "postId") int postId) {
        try {
            this.postService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Xoá bài đăng thành công",
                                    null)
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            System.out.println("Lỗi: " + e);
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

    @GetMapping("/posts/me")
    ResponseEntity<ResponseObjectDTO> getAllPostUser() {

        Page<ResponsePostDTO> data = this.postService.getListPostUser();
        if (!data.getContent().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseObjectDTO(
                            Boolean.TRUE,
                            200,
                            "Lấy danh sách bài đăng của bạn thành công",
                            data));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseObjectDTO(
                        Boolean.FALSE,
                        500,
                        "Lỗi hệ thống",
                        null)
                );
    }
}
