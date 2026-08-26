/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.Conversation;
import com.ndt.AcademicAdvising.repositories.custom.CustomConversationRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer>, CustomConversationRepository {

    @Query("""
    SELECT c
    FROM Conversation c
    WHERE (c.sender.id = :user1 AND c.receiver.id = :user2)
       OR (c.sender.id = :user2 AND c.receiver.id = :user1)
""")
    Optional<Conversation> findConversationBetweenUsers(
            @Param("user1") Integer user1,
            @Param("user2") Integer user2
    );
}
