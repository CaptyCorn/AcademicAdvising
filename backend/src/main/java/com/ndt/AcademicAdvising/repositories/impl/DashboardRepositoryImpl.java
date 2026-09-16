/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.pojo.Book;
import com.ndt.AcademicAdvising.pojo.Post;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.custom.CustomDashboardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public class DashboardRepositoryImpl implements CustomDashboardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tuple> getUsersByMonth(Date start, Date end) {
        return getMonthlyStatistics(
                User.class,
                "createdAt",
                start,
                end
        );
    }

    @Override
    public List<Tuple> getPostsByMonth(
            Date start,
            Date end) {

        return getMonthlyStatistics(
                Post.class,
                "createdAt",
                start,
                end
        );
    }

    @Override
    public List<Tuple> getBooksByMonth(
            Date start,
            Date end) {

        return getMonthlyStatistics(
                Book.class,
                "createdAt",
                start,
                end
        );
    }

    private List<Tuple> getMonthlyStatistics(
            Class<?> entityClass,
            String dateField,
            Date start,
            Date end) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<?> root = query.from(entityClass);

        Expression<Date> monthExpression =
                builder.function(
                        "date_trunc",
                        Date.class,
                        builder.literal("month"),
                        root.get(dateField)
                );

        Expression<String> month =
                builder.function(
                        "to_char",
                        String.class,
                        monthExpression,
                        builder.literal("YYYY-MM")
                );

        Expression<Long> total = builder.count(root);

        query.multiselect(
                month.alias("month"),
                total.alias("total")
        );

        query.where(
                builder.between(
                        root.get(dateField),
                        start,
                        end
                )
        );

        query.groupBy(monthExpression);

        query.orderBy(builder.asc(monthExpression));

        return entityManager.createQuery(query).getResultList();
    }
}
