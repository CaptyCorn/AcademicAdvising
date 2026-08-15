/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.RequestBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookCreateDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDetailDTO;
import com.ndt.AcademicAdvising.pojo.Book;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface BookService {
    Page<ResponseBookDTO> getListBook(Map<String, String> params);
    Page<ResponseBookDTO> getListBookById(Map<String, String> params);
    ResponseBookCreateDTO createBook(RequestBookDTO dto);
    void deleteBook(int bookId);
    ResponseBookDetailDTO getBookId(int bookId);
}
