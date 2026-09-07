/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.RequestBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookCreateDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookDetailDTO;
import com.ndt.AcademicAdvising.dto.ResponseBookIImgDTO;
import com.ndt.AcademicAdvising.dto.ResponseSubjectBookDTO;
import com.ndt.AcademicAdvising.pojo.Book;
import com.ndt.AcademicAdvising.pojo.BookImage;
import com.ndt.AcademicAdvising.pojo.Subject;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.BookImageRepository;
import com.ndt.AcademicAdvising.repositories.BookRepository;
import com.ndt.AcademicAdvising.repositories.SubjectRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.BookService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Service
public class BookServiceImpl implements BookService{
    
    @Autowired
    private BookRepository bookRepo;
    
    @Autowired
    private BookImageRepository bookImgRepo;
    
    @Autowired SubjectRepository subjectRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private Cloudinary cloudinary;
    
    private ResponseBookIImgDTO toDTOImg(BookImage bookImg) {
        ResponseBookIImgDTO dto = new ResponseBookIImgDTO();
        dto.setId(bookImg.getId());
        dto.setImageUrl(bookImg.getImageUrl());
        dto.setCreatedAt(bookImg.getCreatedAt());
        return dto;
    }
    
    private ResponseSubjectBookDTO toDTOSubject(Subject s) {
        ResponseSubjectBookDTO dto = new ResponseSubjectBookDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        return dto;
    }
    
    private ResponseBookDetailDTO toDTODetail(Book b) {
        ResponseBookDetailDTO dto = new ResponseBookDetailDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setDescription(b.getDescription());
        dto.setPrice(b.getPrice());
        dto.setCondition(b.getBookCondition());
        dto.setSubjects(b.getSubjects().stream().map(this::toDTOSubject).collect(Collectors.toSet()));
        dto.setImages(b.getBookImages().stream().map(this::toDTOImg).collect(Collectors.toSet()));
        return dto;
    }
    
    private ResponseBookCreateDTO toDTOCreate (Book b) {
        ResponseBookCreateDTO dto = new ResponseBookCreateDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setDescription(b.getDescription());
        dto.setPrice(b.getPrice());
        dto.setCondition(b.getBookCondition());
        dto.setStatus(b.getBookStatus());
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }
    
    private ResponseBookDTO toDTO (Book b) {
        ResponseBookDTO dto = new ResponseBookDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setPrice(b.getPrice());
        dto.setCondition(b.getBookCondition());
        dto.setImage(b.getBookImages().stream().map(this::toDTOImg).findFirst().orElse(null));
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }
    
    private PageResponseDTO<ResponseBookDTO> toPageDTO(Page<ResponseBookDTO> page) {
        PageResponseDTO<ResponseBookDTO> dto = new PageResponseDTO<>();
        dto.setContent(page.getContent());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }
    
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(username);
    }

    @Override
    public PageResponseDTO<ResponseBookDTO> getListBook(Map<String, String> params) {
        return toPageDTO(this.bookRepo.getListBook(params).map(this::toDTO));
    }

    @Override
    public ResponseBookCreateDTO createBook(RequestBookDTO dto) {
        Book b = new Book();
        b.setName(dto.getName());
        b.setDescription(dto.getDescription());
        b.setPrice(dto.getPrice());
        b.setBookCondition(dto.getCondition());
        b.setUser(getCurrentUser());
        
        Set<Subject> subjects = new HashSet<>();
        
        System.out.println("Thêm môn");
        if (dto.getSubjectIds() != null) {
            for (Integer subId : dto.getSubjectIds()) {
                Subject s = this.subjectRepo.findById(subId)
                        .orElseThrow(
                                () -> new IllegalArgumentException("Môn học id = " + subId + " không có.")
                        );
                subjects.add(s);
            }          
        }
        b.setSubjects(subjects);
        System.out.println("xong Thêm môn");
        
        Set<BookImage> images = new HashSet<>();
        
        System.out.println("Thêm ảnh");
        if (dto.getFiles() != null) {
            for (MultipartFile f : dto.getFiles()) {
                try {
                    Map res = this.cloudinary.uploader().upload(
                            f.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", "Academic/BookImage",
                                    "resource_type", "image")
                    );
                    
                    BookImage image = new BookImage();
                    image.setImageUrl(res.get("secure_url").toString());
                    image.setImagePublicId(res.get("public_id").toString());
                    image.setBook(b);
                    
                    images.add(image);
                } catch (IOException ex) {
                    System.getLogger(BookServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                
            }
        }
        
        b.setBookImages(images);
        System.out.println("xong Thêm ảnh");
        
        return toDTOCreate(this.bookRepo.save(b));
        
    }

    @Override
    public void deleteBook(int bookId) {
        Book b = this.bookRepo.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
        User u = getCurrentUser();
        if (Objects.equals(b.getUser().getId(), u.getId())) {
            List<BookImage> images = this.bookImgRepo.findAllByBookId(bookId);
            for(BookImage img : images) {
                try {
                    this.cloudinary.uploader().destroy(
                            img.getImagePublicId(),
                            ObjectUtils.emptyMap()
                    );
                } catch (IOException ex) {
                    System.getLogger(BookServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
            
            this.bookRepo.deleteById(bookId);
        }
        else throw new IllegalArgumentException("Không thể xoá sách của người khác");
    }

    @Override
    public ResponseBookDetailDTO getBookId(int bookId) {
        Book b = this.bookRepo.findById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Sách không tồn tại")
        );
        return toDTODetail(b);
    }

    @Override
    public PageResponseDTO<ResponseBookDTO> getListBookByUserId(Map<String, String> params) {
        User u = getCurrentUser();
        return toPageDTO(this.bookRepo.getListBookById(u.getId(), params).map(this::toDTO));
    }
    
    
}
