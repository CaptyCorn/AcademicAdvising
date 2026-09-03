/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.RequestBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookCreateDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDetailDTO;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface BookService {
    PageResponseDTO<ResponseBookDTO> getListBook(Map<String, String> params);
    PageResponseDTO<ResponseBookDTO> getListBookByUserId(Map<String, String> params);
    ResponseBookCreateDTO createBook(RequestBookDTO dto);
    void deleteBook(int bookId);
    ResponseBookDetailDTO getBookId(int bookId);
}
