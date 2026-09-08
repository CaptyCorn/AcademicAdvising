/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.custom;

import jakarta.persistence.Tuple;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ngodo
 */
public interface CustomDashboardRepository {
    List<Tuple> getUsersByMonth(Date start, Date end);

    List<Tuple> getPostsByMonth(Date start, Date end);

    List<Tuple> getBooksByMonth(Date start, Date end);
}
