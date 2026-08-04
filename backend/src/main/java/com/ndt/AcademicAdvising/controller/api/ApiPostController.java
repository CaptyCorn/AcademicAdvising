/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import com.ndt.AcademicAdvising.services.PostService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@Controller
@RestController
@RequestMapping("/ouacademic")
public class ApiPostController {
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/posts")
    ResponseEntity<ResponseObjectDTO> getAllPost() {
        Page<ResponsePostDTO> data = this.postService.getListPost();
        if (data.getContent().size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObjectDTO("OK", "Query success", data));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectDTO("Not data", "Have not data", ""));
    }
    
    @PostMapping("/post")
    ResponseEntity<ResponseObjectDTO> insertPost(@RequestBody Map<String, String> p) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObjectDTO("CREATED", "Insert success", this.postService.addPost(p.get("content"))));
    }
    
    @DeleteMapping("/posts/{postId}")
    ResponseEntity<ResponseObjectDTO> deletePost(@PathVariable(value = "postId") int postId) {
        if (this.postService.existPost(postId)) {
            this.postService.deletePost(postId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .body(new ResponseObjectDTO("Success", "Delete post successfully", ""));
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObjectDTO("Fail", "Can not post to delete with id = " + postId, ""));
    }
}
