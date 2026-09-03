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
                                    Boolean.TRUE,
                                    200,
                                    "Lấy danh sách sách thành công",
                                    this.bookService.getListBook(params)
                            )
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
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
                                    Boolean.TRUE,
                                    200,
                                    "Lấy thông tin danh sách thành công",
                                    this.bookService.getBookId(bookId)
                            )
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
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
                                    Boolean.TRUE,
                                    200,
                                    "Lấy danh sách của người dùng thành công",
                                    this.bookService.getListBookByUserId(params)
                            )
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }

    @PostMapping("/book")
    ResponseEntity<?> insert(@Valid @ModelAttribute RequestBookDTO dto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    201,
                                    "Thêm sách thành công",
                                    this.bookService.createBook(dto)
                            )
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
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
                                    Boolean.TRUE,
                                    200,
                                    "Xoá sách thành công",
                                    null
                            )
                    );
        } catch (IllegalArgumentException i) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    400,
                                    i.getMessage(),
                                    null)
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.FALSE,
                                    500,
                                    "Lỗi hệ thống",
                                    null)
                    );
        }
    }
}
