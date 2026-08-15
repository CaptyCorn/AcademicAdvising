/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.RequestBookDTO;
import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.BookService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/ouacademic")
public class ApiBookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/books")
    ResponseEntity<ResponseObjectDTO> list(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ResponseObjectDTO(
                                "Success", 
                                "Get books successfully", 
                                this.bookService.getListBook(params)
                        )
                );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ResponseObjectDTO(
                                "Fail", 
                                i.getMessage(), 
                                null
                        )
                );
        }
    }
    
    @GetMapping("/books/{bookId}")
    ResponseEntity<ResponseObjectDTO> bookDetail(@PathVariable(name = "bookId") int bookId) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ResponseObjectDTO(
                                "Success", 
                                "Get book detail successfully", 
                                this.bookService.getBookId(bookId)
                        )
                );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ResponseObjectDTO(
                                "Fail", 
                                i.getMessage(), 
                                null
                        )
                );
        }
    }
    
    @GetMapping("/books/user")
    ResponseEntity<ResponseObjectDTO> bookUser(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ResponseObjectDTO(
                                "Success", 
                                "Get books successfully", 
                                this.bookService.getListBookById(params)
                        )
                );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ResponseObjectDTO(
                                "Fail", 
                                i.getMessage(), 
                                null
                        )
                );
        }
    }
    
    @PostMapping("/book")
    ResponseEntity<?> insert(@Valid @ModelAttribute RequestBookDTO dto) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ResponseObjectDTO(
                                "Success", 
                                "Insert books successfully", 
                                this.bookService.createBook(dto)
                        )
                );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ResponseObjectDTO(
                                "Fail", 
                                i.getMessage(), 
                                null
                        )
                );
        }
    }
    
    @DeleteMapping("/books/{bookId}")
    ResponseEntity<ResponseObjectDTO> delete(@PathVariable(name = "bookId") int bookId) {
        try {
            this.bookService.deleteBook(bookId);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ResponseObjectDTO(
                                "Success", 
                                "Delete books successfully", 
                                null
                        )
                );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ResponseObjectDTO(
                                "Fail", 
                                i.getMessage(), 
                                null
                        )
                );
        }
    }
}
