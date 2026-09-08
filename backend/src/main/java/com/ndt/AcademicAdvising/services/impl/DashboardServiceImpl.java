/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.MonthlyStatisticDTO;
import com.ndt.AcademicAdvising.dto.ResponseDashboardDTO;
import com.ndt.AcademicAdvising.dto.ResponseMonthlyStaticDTO;
import com.ndt.AcademicAdvising.repositories.BookRepository;
import com.ndt.AcademicAdvising.repositories.CommentRepository;
import com.ndt.AcademicAdvising.repositories.PostRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.repositories.custom.CustomDashboardRepository;
import com.ndt.AcademicAdvising.services.DashboardService;
import jakarta.persistence.Tuple;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private CustomDashboardRepository cusDashboardRepo;

    @Override
    public ResponseDashboardDTO getDashboard() {
        ResponseDashboardDTO dto = new ResponseDashboardDTO();

        dto.setTotalUsers(this.userRepo.count());
        dto.setTotalPosts(this.postRepo.count());
        dto.setTotalComments(this.commentRepo.count());
        dto.setTotalBooks(this.bookRepo.count());

        return dto;
    }

    @Override
    public ResponseMonthlyStaticDTO getRevenue() {
        ResponseMonthlyStaticDTO dto = new ResponseMonthlyStaticDTO();
        YearMonth currentMonth
                = YearMonth.now();

        YearMonth startMonth
                = currentMonth.minusMonths(11);

        Date start
                = Date.from(
                        startMonth
                                .atDay(1)
                                .atStartOfDay(
                                        ZoneId.systemDefault()
                                )
                                .toInstant()
                );

        Date end
                = Date.from(
                        currentMonth
                                .plusMonths(1)
                                .atDay(1)
                                .atStartOfDay(
                                        ZoneId.systemDefault()
                                )
                                .toInstant()
                );

        List<MonthlyStatisticDTO> users = cusDashboardRepo.getUsersByMonth(start, end).stream().map(this::toMonthlyDTO).toList();
        List<MonthlyStatisticDTO> posts = cusDashboardRepo.getPostsByMonth(start, end).stream().map(this::toMonthlyDTO).toList();
        List<MonthlyStatisticDTO> books = cusDashboardRepo.getBooksByMonth(start, end).stream().map(this::toMonthlyDTO).toList();

        dto.setUsersByMonth(users);
        dto.setPostsByMonth(posts);
        dto.setBooksByMonth(books);
        return dto;
    }

    private MonthlyStatisticDTO toMonthlyDTO(Tuple tuple) {
        return new MonthlyStatisticDTO(
                tuple.get("month", String.class),
                tuple.get("total", Long.class)
        );
    }

}
