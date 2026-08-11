/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.ResponseMajorDTO;
import com.ndt.AcademicAdvising.pojo.Major;
import com.ndt.AcademicAdvising.repositories.MajorRepository;
import com.ndt.AcademicAdvising.services.MajorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class MajorServiceImpl implements MajorService{
    
    @Autowired
    private MajorRepository majorRepo;
    
    private ResponseMajorDTO toDTO(Major m) {
        ResponseMajorDTO dto = new ResponseMajorDTO();
        dto.setId(m.getId());
        dto.setName(m.getName());
        return dto;
    }

//    @Override
//    public List<ResponseMajorDTO> getMajors() {
//        return this.majorRepo.findAll()
//    }

    @Override
    public ResponseMajorDTO getMajorById(int majorId) {
        Major m = this.majorRepo.findById(majorId).orElseThrow(
                () -> new IllegalArgumentException("Không tìm thấy major với id = " + majorId));
        return toDTO(m);
    }

    @Override
    public ResponseMajorDTO createMajor(String name) {
        if (!name.isBlank()) {
            Major major = this.majorRepo.findByName(name);
            if (major != null) {
                Major m = new Major();
                m.setName(name);
                 return toDTO(this.majorRepo.save(m));
            }
            else throw new IllegalArgumentException("Ngành " + name + " đã tồn tài.");
        } 
        else throw new IllegalArgumentException("Tên ngành không được để trống");
        
    }

    @Override
    public void deleteMajor(int majorId) {
        if (this.majorRepo.existsById(majorId)) {
            this.majorRepo.deleteById(majorId);
        }
        else throw new IllegalArgumentException("Không tồn tại ngành với id = " + majorId);
    }
    
}
