/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndt.AcademicAdvising.dto.RequestUserLoginDTO;
import com.ndt.AcademicAdvising.dto.RequestUserRegisterDTO;
import com.ndt.AcademicAdvising.enums.UserRole;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;
    

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.userRepo.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getUserRole().toString()));

        return new org.springframework.security.core.userdetails.User(u.getUsername(),
                 u.getPassword(), authorities);
    }

    public User mapToEntity(RequestUserRegisterDTO dto) {
        User u = new User();
        u.setName(dto.getName());
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setEmail(dto.getEmail());
        u.setStudentCode(dto.getStudentCode());
        u.setPhone(dto.getPhone());
        u.setUserRole(UserRole.ROLE_STUDENT);
        return u;
    }

    @Override
    public void addUser(RequestUserRegisterDTO dto) {
        User u = mapToEntity(dto);

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(
                        dto.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto")
                );

                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.getLogger(UserServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

        this.userRepo.save(u);
    }

}
