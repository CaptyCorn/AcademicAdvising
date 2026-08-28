/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import com.ndt.AcademicAdvising.pojo.Post;
import com.ndt.AcademicAdvising.repositories.custom.CustomPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, CustomPostRepository{
    Page<Post> findAllPostBy(Pageable pageable);
    Page<Post> findAllByUserIdOrderByCreatedAtDesc(Pageable pageable, int userId);
}
