/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.ResponseMajorDTO;
import java.util.List;

/**
 *
 * @author ngodo
 */
public interface MajorService {
//    List<ResponseMajorDTO> getMajors();
    ResponseMajorDTO getMajorById(int majorId);
    ResponseMajorDTO createMajor(String name);
    void deleteMajor(int majorId);
    
}
