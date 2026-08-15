/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.custom;

import com.ndt.AcademicAdvising.pojo.Major;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface CustomMajorRepository {
    Page<Major> getListMajor(Map<String, String> params);
}
