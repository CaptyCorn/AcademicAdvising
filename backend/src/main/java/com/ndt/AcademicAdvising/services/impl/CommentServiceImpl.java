/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.ResponseCommentDTO;
import com.ndt.AcademicAdvising.pojo.Comment;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.CommentRepository;
import com.ndt.AcademicAdvising.repositories.PostRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.CommentService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class CommentServiceImpl implements CommentService{
    
    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private PostRepository postRepo;
    
    private ResponseCommentDTO toDTO(Comment c) {
        ResponseCommentDTO dto = new ResponseCommentDTO();
        dto.setId(c.getId());
        dto.setContent(c.getContent());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setNameUserComment(c.getUser().getName());
        dto.setPostId(c.getPost().getId());
        return dto;
    }
    
    private User getCurrentUser() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(currentUser);
    }
    
    @Override
    public Comment getComment(int postId) {
        return this.commentRepo
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));
    }

    @Override
    public Page<ResponseCommentDTO> getComments(int postId) {
        Pageable pageable = PageRequest.of(0, 5);
        return this.commentRepo.findAllByPostIdOrderByCreatedAtDesc(postId, pageable).map(this::toDTO);
    }

    @Override
    public ResponseCommentDTO addComment(String content, int postId) {
        if (content == null || content.isBlank())
            throw new IllegalArgumentException("Nội dung không được để trống.");
        
        Comment c = new Comment();
        c.setContent(content);
        c.setPost(this.postRepo.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bài đăng không tồn tại")));
        c.setUser(getCurrentUser());
        
        return toDTO(this.commentRepo.save(c));
    }

    @Override
    public ResponseCommentDTO updateComment(int commentId, String content) {     
        if (content == null || content.isBlank())
            throw new IllegalArgumentException("Nội dung không được để trống.");
        
        Comment c = getComment(commentId); 
        User currentUser = getCurrentUser();
        if (!Objects.equals(currentUser.getId(), c.getUser().getId()))
            throw new IllegalArgumentException("Không thể xoá bình luận của người khác.");
        
        c.setContent(content);
        return toDTO(this.commentRepo.save(c));
    }

    @Override
    public void deleteComment(int commentId) {
        Comment c = getComment(commentId);        
        User currentUser = getCurrentUser();
        if (!Objects.equals(currentUser.getId(), c.getUser().getId()))
            throw new IllegalArgumentException("Không thể xoá bình luận của người khác.");
        
        this.commentRepo.deleteById(commentId);
        
    }

    @Override
    public void addCommentAI(String content, int postId) {
        if (content == null || content.isBlank())
            throw new IllegalArgumentException("Nội dung không được để trống.");
        
        Comment c = new Comment();
        c.setContent(content);
        c.setPost(this.postRepo.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bài đăng không tồn tại")));
        c.setUser(this.userRepo.findByUsername("aiagent"));  
        this.commentRepo.save(c);
    }

    
    
}
