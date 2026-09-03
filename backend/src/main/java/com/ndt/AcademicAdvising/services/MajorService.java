/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseMajorDTO;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface MajorService {
    PageResponseDTO<ResponseMajorDTO> getMajors(Map<String, String> params);
    ResponseMajorDTO getMajorById(int majorId);
    ResponseMajorDTO createMajor(String name);
    void deleteMajor(int majorId);
    
}
