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
    Boolean existPost(int postId);
    void deletePost(int postId);
}
