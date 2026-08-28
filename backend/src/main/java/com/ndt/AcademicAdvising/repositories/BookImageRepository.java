/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.BookImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Integer>{
    List<BookImage> findAllByBookId(int bookId);
}
