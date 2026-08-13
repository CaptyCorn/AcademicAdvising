/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.Subject;
import com.ndt.AcademicAdvising.repositories.customes.CustomeSubjectRepository;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer>, CustomeSubjectRepository{
    boolean existsByName(String name);
}
