/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.pojo.Post;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.PostRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.PostService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    private ResponsePostDTO toDTO(Post post) {
        ResponsePostDTO dto = new ResponsePostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUser(new ResponseUserDTO(post.getUser().getName(), 
                                        post.getUser().getUsername(), 
                                        post.getUser().getEmail(), 
                                        post.getUser().getAvatar(), 
                                        post.getUser().getStudentCode()));
        return dto;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(username);
    }

    @Override
    public Page<ResponsePostDTO> getListPost() {
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.postRepo.findAllPostBy(pageable).map(this::toDTO);
    }

    @Override
    public ResponsePostDTO addPost(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Nội dung không được để trống.");
        } else {
            Post post = new Post();
            post.setContent(content);
            post.setUser(getCurrentUser());
            this.postRepo.save(post);
            return toDTO(post);
        }
    }

    @Override
    public ResponsePostDTO updatePost(int postId, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Nội dung không được để trống.");
        }

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài viết."));

        User currentUser = getCurrentUser();

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Không thể cập nhật bài đăng của người khác.");
        }
        
        post.setContent(content);
        postRepo.save(post);
        return toDTO(post);
    }

    @Override
    public void deletePost(int postId) {
        Post p = this.postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài đăng không tồn tại."));
        
        User currentUser = getCurrentUser();
        
        if (Objects.equals(p.getUser().getId(), currentUser.getId())) {
            this.postRepo.delete(p);
        } else {
            throw new IllegalArgumentException("Không thể xoá bài đăng của người khác.");
        }
    }

    @Override
    public Page<ResponsePostDTO> getListPostUser() {
        User currentUser = getCurrentUser();
        Pageable pageable = PageRequest.of(0, 10);
        
        return this.postRepo.findAllByUserIdOrderByCreatedAtDesc(pageable, currentUser.getId())
                .map(this::toDTO);
    }

}
