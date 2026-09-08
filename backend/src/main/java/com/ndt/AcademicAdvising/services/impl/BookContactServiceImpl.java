/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.enums.BookStatus;
import com.ndt.AcademicAdvising.enums.MessageType;
import com.ndt.AcademicAdvising.pojo.Book;
import com.ndt.AcademicAdvising.pojo.Conversation;
import com.ndt.AcademicAdvising.pojo.ConversationBook;
import com.ndt.AcademicAdvising.pojo.Message;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.BookRepository;
import com.ndt.AcademicAdvising.repositories.ConversationBookRepository;
import com.ndt.AcademicAdvising.repositories.ConversationRepository;
import com.ndt.AcademicAdvising.repositories.MessageRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.BookContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class BookContactServiceImpl implements BookContactService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private ConversationBookRepository conversationBookRepository;
    
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Integer contactSeller(Integer bookId, String username) {
        // 1. Người mua hiện tại
        User buyer = userRepository.findByUsername(username);

        // 2. Tìm sách
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy sách"));

        // 3. Người bán chính là chủ sở hữu sách
        User seller = book.getUser();

        // 4. Không cho tự liên hệ chính mình
        if (buyer.getId().equals(seller.getId())) {
            throw new RuntimeException(
                    "Bạn không thể liên hệ với chính mình"
            );
        }

        // 5. Kiểm tra trạng thái sách
        if (book.getBookStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException(
                    "Sách này hiện không còn khả dụng"
            );
        }

        // 6. Tìm conversation giữa buyer và seller
        Conversation conversation =
                conversationRepository
                        .findConversationBetweenUsers(
                                buyer.getId(),
                                seller.getId()
                        )
                        .orElseGet(() -> {

                            Conversation c = new Conversation();

                            c.setSender(buyer);
                            c.setReceiver(seller);

                            // Quan trọng vì lastSender là NOT NULL
                            c.setLastSender(buyer);

                            return conversationRepository.save(c);
                        });

        // 7. Conversation đã từng liên quan tới sách này chưa?
        boolean existed =
                conversationBookRepository
                        .existsByConversationIdAndBookId(
                                conversation.getId(),
                                book.getId()
                        );

        if (!existed) {

            // 8. Đánh dấu sách này thuộc conversation
            ConversationBook conversationBook =
                    new ConversationBook();

            conversationBook.setConversation(conversation);
            conversationBook.setBook(book);

            conversationBookRepository.save(conversationBook);
        }

        // 9. Tạo BOOK message
        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(buyer);

        message.setMessageType(MessageType.BOOK);
        message.setBook(book);

        // Không cần content
        message.setContent(null);

        messageRepository.save(message);

        // 10. Cập nhật conversation preview
        conversation.setLastSender(buyer);
        conversation.setLastMessage("Đã gửi một cuốn sách");

        conversationRepository.save(conversation);

        return conversation.getId();
    }
    
}
