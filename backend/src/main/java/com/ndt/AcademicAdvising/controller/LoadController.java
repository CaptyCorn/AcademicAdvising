/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller;

import com.ndt.AcademicAdvising.utils.EmbeddingComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/ouacademic")
public class LoadController {
    @Autowired
    private EmbeddingComponent component;
    
    @GetMapping("/loading")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> load() {
        try {
            System.out.println("Loading document start");
            component.loadDocument();
            System.out.println("Loading document end");
            return new ResponseEntity<>("Load success", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Loading document start");
            component.loadDocument();
            System.out.println("Loading document end");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
