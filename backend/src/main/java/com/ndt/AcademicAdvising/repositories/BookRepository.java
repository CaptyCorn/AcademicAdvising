/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.Book;
import com.ndt.AcademicAdvising.repositories.custom.CustomBookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, CustomBookRepository{
    
}
