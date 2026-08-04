/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.RequestPostDTO;
import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import com.ndt.AcademicAdvising.pojo.Post;
import com.ndt.AcademicAdvising.repositories.PostRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.PostService;
import java.util.Optional;
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
public class PostServiceImpl implements PostService{
    
    @Autowired
    private PostRepository postRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    public ResponsePostDTO toDTO(Post post) {
        ResponsePostDTO dto = new ResponsePostDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    @Override
    public Page<ResponsePostDTO> getListPost() {
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.postRepo.findAllPostBy(pageable).map(this::toDTO);
    }

    @Override
    public ResponsePostDTO addPost(String content) {
        Post post = new Post();
        post.setContent(content);
//        post.setUser(this.userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        post.setUser(this.userRepo.findByUsername("ngodothanh"));
        this.postRepo.save(post);
        return toDTO(post);
    }
    
    @Override
    public Boolean existPost(int postId) {
        return this.postRepo.existsById(postId);
    }

    @Override
    public void deletePost(int postId) {
        this.postRepo.deleteById(postId);
    }



}
