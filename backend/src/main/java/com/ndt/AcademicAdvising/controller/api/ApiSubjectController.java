/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.SubjectService;
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
@RequestMapping("/ouacademic")
public class ApiSubjectController {
    
    @Autowired
    private SubjectService subjectService;
    
    @GetMapping("/subjects")
    ResponseEntity<ResponseObjectDTO> list(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObjectDTO(
                        "Success", 
                        "Get subjects successfully", 
                        this.subjectService.getSubjects(params)));
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObjectDTO(
                        "Fail", 
                        i.getMessage(), 
                        null));
        }
    }
    
    @PostMapping("/subject")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObjectDTO> create(@RequestBody Map<String, String> data) {
        try {
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObjectDTO(
                        "Success", 
                        "Create subject successfully", 
                        this.subjectService.createSubject(data)));
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObjectDTO(
                        "Fail", 
                        i.getMessage(), 
                        null));
        }
    }
    
    @DeleteMapping("/subjects/{subjectId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObjectDTO> delete(@PathVariable(name = "subjectId") int subjectId) {
        try {
            this.subjectService.deleteSubject(subjectId);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObjectDTO(
                        "Success", 
                        "Delete subject successfully", 
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
