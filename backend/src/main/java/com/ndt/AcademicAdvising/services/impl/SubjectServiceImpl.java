/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.ResponseSubjectDTO;
import com.ndt.AcademicAdvising.pojo.Major;
import com.ndt.AcademicAdvising.pojo.Subject;
import com.ndt.AcademicAdvising.repositories.MajorRepository;
import com.ndt.AcademicAdvising.repositories.SubjectRepository;
import com.ndt.AcademicAdvising.services.SubjectService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class SubjectServiceImpl implements SubjectService{
    
    @Autowired
    private SubjectRepository subjectRepo;
    
    @Autowired
    private MajorRepository majorRepo;
    
    private ResponseSubjectDTO toDTO(Subject s) {
        ResponseSubjectDTO dto = new ResponseSubjectDTO();
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        dto.setMajorId(s.getMajor().getId());
        return dto;
    }

    @Override
    public ResponseSubjectDTO createSubject(Map<String, String> data) {
        String name = data.get("name");
        if (!this.subjectRepo.existsByName(name)) {
            String description = data.get("description");
            int majorId = Integer.parseInt(data.get("majorId"));
            
            Major m = this.majorRepo.findById(majorId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ngành có id = " + majorId));
            
            Subject s = new Subject();
            s.setName(name);
            s.setDescription(description);
            s.setMajor(m);
            return toDTO(this.subjectRepo.save(s));
        }
        else throw new IllegalArgumentException("Môn này đã tồn tại");
    }

    @Override
    public Page<ResponseSubjectDTO> getSubjects(Map<String, String> params) {
        return this.subjectRepo.getListSubject(params).map(this::toDTO);
    }

    @Override
    public void deleteSubject(int subjectId) {
        if (this.subjectRepo.existsById(subjectId)) {
            this.subjectRepo.deleteById(subjectId);
        }
        else throw new IllegalArgumentException("Không tồn tại môn học này");
    }

    @Override
    public Subject getSubjectId(int subjectId) {
        return this.subjectRepo.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("Không tồn tại môn học này")
        );
    }
    
    
    
}
