/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.RequestUserLoginDTO;
import com.ndt.AcademicAdvising.dto.RequestUserRegisterDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author ngodo
 */
public interface UserService extends UserDetailsService{
    ResponseUserDTO addUser(RequestUserRegisterDTO u);
}
