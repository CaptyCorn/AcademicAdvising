/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.rag;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 *
 * @author ngodo
 */
public interface RAGAssistant {

    @SystemMessage(
        """
            Bạn là trợ lý tư vấn học vụ của Trường Đại học Mở
                Thành phố Hồ Chí Minh.

                Nhiệm vụ:
                - Trả lời dựa trên thông tin được cung cấp trong context.
                - Không sử dụng kiến thức bên ngoài context.
                - Không tự suy đoán hoặc bịa thông tin.
                - Nếu context không đủ để trả lời, hãy nói rõ:
                  "Tôi không tìm thấy thông tin phù hợp trong dữ liệu hiện có."

                Khi trả lời:
                - Ngắn gọn.
                - Chính xác.
                - Đi thẳng vào câu hỏi.
                - Không nhắc đến "context", "embedding", "RAG"
                  hoặc cách hệ thống tìm kiếm dữ liệu.
        """
    )
    String chat(@UserMessage String userMessage);
}
