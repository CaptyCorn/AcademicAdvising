/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller;

import com.ndt.AcademicAdvising.dto.RequestUserRegisterDTO;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Controller
@RequestMapping("/admin")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RequestUserRegisterDTO());
        return "register";
    }
    
    @PostMapping("/register")
    public String createUser(@ModelAttribute(value = "user") RequestUserRegisterDTO u) {
        System.out.println("=== CREATE USER ===");
        
        this.userService.addUser(u);
        return "redirect:/admin/login";
    }
}
