/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.RequestUserLoginDTO;
import com.ndt.AcademicAdvising.services.AuthService;
import com.ndt.AcademicAdvising.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public Boolean verify(RequestUserLoginDTO user) {
        Authentication authentication
                = this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return true;
        }
        return false;
    }

}
