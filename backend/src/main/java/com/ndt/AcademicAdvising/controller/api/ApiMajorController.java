/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.controller.api;

import com.ndt.AcademicAdvising.dto.ResponseObjectDTO;
import com.ndt.AcademicAdvising.services.MajorService;
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
@RequestMapping("ouacademic")
public class ApiMajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping("/majors")
    ResponseEntity<ResponseObjectDTO> list(@RequestParam Map<String, String> params) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Lấy danh sách ngành học thành công",
                                    this.majorService.getMajors(params)));
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

    @PostMapping("/major")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObjectDTO> insert(@RequestBody Map<String, String> p) {
        String name = p.get("name");
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    201,
                                    "Thêm ngành học thành công",
                                    this.majorService.createMajor(name)));

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

    @DeleteMapping("/majors/{majorId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ResponseObjectDTO> delete(@PathVariable(name = "majorId") int majorId) {
        try {
            this.majorService.deleteMajor(majorId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            new ResponseObjectDTO(
                                    Boolean.TRUE,
                                    200,
                                    "Xoá ngành học thành công",
                                    null));

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
