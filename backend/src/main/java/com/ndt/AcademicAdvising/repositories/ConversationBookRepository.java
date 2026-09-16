/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.ConversationBook;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ngodo
 */
public interface ConversationBookRepository extends JpaRepository<ConversationBook, Integer>{
    Optional<ConversationBook> findByConversationIdAndBookId(
            Integer conversationId,
            Integer bookId
    );

    boolean existsByConversationIdAndBookId(
            Integer conversationId,
            Integer bookId
    );
}
