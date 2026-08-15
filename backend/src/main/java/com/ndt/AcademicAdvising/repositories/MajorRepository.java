/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.Major;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ndt.AcademicAdvising.repositories.custom.CustomMajorRepository;

/**
 *
 * @author ngodo
 */
@Repository
public interface MajorRepository extends JpaRepository<Major, Integer>, CustomMajorRepository{
    Major findByName(String name);
}
