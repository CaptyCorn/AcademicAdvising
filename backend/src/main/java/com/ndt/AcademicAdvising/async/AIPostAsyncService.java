/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.async;

import com.ndt.AcademicAdvising.enums.AIStatus;
import com.ndt.AcademicAdvising.pojo.Post;
import com.ndt.AcademicAdvising.repositories.PostRepository;
import com.ndt.AcademicAdvising.services.AIService;
import com.ndt.AcademicAdvising.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class AIPostAsyncService {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PostRepository postRepo;
    
    @Async
    public void processPost(int postId) {
        Post p = this.postRepo.findById(postId).orElseThrow();
        try {
            
            
            p.setAiStatus(AIStatus.PROCESSING);
            this.postRepo.save(p);
            
            String answer = this.aiService.getMessage(p.getContent());
            this.commentService.addCommentAI(answer, postId);
            
            p.setAiStatus(AIStatus.REPLIED);
            this.postRepo.save(p);
            
        } catch (Exception e) {
            p.setAiStatus(AIStatus.FAILED);
            this.postRepo.save(p);
            
            e.printStackTrace();
        }
    }
}
