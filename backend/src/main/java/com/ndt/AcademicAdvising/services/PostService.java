/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface PostService {
    Page<ResponsePostDTO> getListPost();
    ResponsePostDTO addPost(String content);
    void deletePost(int postId);
    ResponsePostDTO updatePost(int postId, String content);
    public Page<ResponsePostDTO> getListPostUser();
}
