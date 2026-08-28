/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.custom;

import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface CustomPostRepository {
    Page<ResponsePostDTO> getListPost(Map<String, String> params);
    ResponsePostDTO getPostById(int postId);
}
