/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.MajorService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("ouacademic")
public class ApiMajorController {
    
    @Autowired
    private MajorService majorService;
    
    @GetMapping("/majors")
    ResponseEntity<ResponseObjectDTO> list(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObjectDTO(
                            "OK", 
                            "Get majors successfully", 
                            this.majorService.getMajors(params)));
        } catch (IllegalArgumentException i) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObjectDTO(
                            "Fail", 
                            i.getMessage(), 
                            null));
        }
    }
    
    @PostMapping("/major")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObjectDTO> insert(@RequestBody Map<String, String> p) {
        String name = p.get("name");
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseObjectDTO(
                            "Success", 
                            "Insert major successfully", 
                            this.majorService.createMajor(name)));
                    
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObjectDTO(
                            "Fail", 
                            i.getMessage(), 
                            null));
        }
    }
    
    @DeleteMapping("/majors/{majorId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObjectDTO> delete(@PathVariable(name = "majorId") int majorId) {
        try {
            this.majorService.deleteMajor(majorId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseObjectDTO(
                            "Success", 
                            "Delete major successfully", 
                            null));
                    
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObjectDTO(
                            "Fail", 
                            i.getMessage(), 
                            null));
        }
    }
}
