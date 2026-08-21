/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndt.AcademicAdvising.dto.RequestUpdateUserDTO;
import com.ndt.AcademicAdvising.dto.RequestUserRegisterDTO;
import com.ndt.AcademicAdvising.dto.ResponseProfileUserDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.enums.UserRole;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${cloudinary.image.default}")
    private String imageDefault;
    

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    
    private ResponseUserDTO toDTO(User u) {
        ResponseUserDTO dto = new ResponseUserDTO();
        dto.setName(u.getName());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setStudentCode(u.getStudentCode());
        dto.setAvatar(u.getAvatar());
        
        return dto;
    }
    
    private ResponseProfileUserDTO toProfileDTO(User u) {
        ResponseProfileUserDTO dto = new ResponseProfileUserDTO();
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setStudentCode(u.getStudentCode());
        dto.setPhone(u.getPhone());
        dto.setAvatar(u.getAvatar());
        dto.setRole(u.getUserRole());
        
        return dto;
    }

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
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setEmail(dto.getEmail());
        u.setStudentCode(dto.getStudentCode());
        u.setUserRole(UserRole.ROLE_STUDENT);
        return u;
    }

    @Override
    public ResponseUserDTO addUser(RequestUserRegisterDTO dto) {
        User u = mapToEntity(dto);

//        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
//            try {
//                Map res = this.cloudinary.uploader().upload(
//                        dto.getFile().getBytes(),
//                        ObjectUtils.asMap(
//                                "folder", "Academic/ImageUser",
//                                "public_id", "user" + dto.getUsername(),
//                                "overwrite", true,
//                                "resource_type", "auto")
//                );
//                u.setAvatar(res.get("secure_url").toString());
//                u.setAvatarPublicId(res.get("public_id").toString());
//            } catch (IOException ex) {
//                System.getLogger(UserServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//            }
//        }
//        else {
//            u.setAvatar(this.imageDefault);
//        }

        return toDTO(this.userRepo.save(u));
    }

    @Override
    public ResponseProfileUserDTO getProfile(String username) {
        User u = this.userRepo.findByUsername(username);
        return toProfileDTO(u);
    }

    @Override
    public ResponseProfileUserDTO updateUser(String username, RequestUpdateUserDTO userDTO) {
        User u = this.userRepo.findByUsername(username);
        if (u != null) {
            u.setFirstName(userDTO.getFirstName());
            u.setLastName(userDTO.getLastName());
            u.setPhone(userDTO.getPhone());
            
            if (userDTO.getFile() != null && !userDTO.getFile().isEmpty()) {
            try {
                this.cloudinary.uploader().destroy(u.getAvatarPublicId(), ObjectUtils.emptyMap());
                Map res = this.cloudinary.uploader().upload(
                        userDTO.getFile().getBytes(),
                        ObjectUtils.asMap(
                                "folder", "Academic/ImageUser",
                                "public_id", "user" + u.getUsername(),
                                "overwrite", true,
                                "resource_type", "auto")
                );
                u.setAvatar(res.get("secure_url").toString());
                u.setAvatarPublicId(res.get("public_id").toString());
                } catch (IOException ex) {
                    System.getLogger(UserServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }
        return toProfileDTO(this.userRepo.save(u));
    }
    
    

}
