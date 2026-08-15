/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.ResponseSubjectDTO;
import com.ndt.AcademicAdvising.pojo.Subject;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface SubjectService {
    ResponseSubjectDTO createSubject(Map<String, String> data);
    Page<ResponseSubjectDTO> getSubjects(Map<String, String> params);
    void deleteSubject(int subjectId);
    Subject getSubjectId(int subjectId);
}
