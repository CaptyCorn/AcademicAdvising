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
                Bạn là trợ lý đắc lực chuyên giải đáp các thắc mắc về đào tạo và tuyển sinh tại Trường Đại học Mở Thành phố Hồ Chí Minh.
                Hãy trả lời mọi câu hỏi một cách tốt nhất có thể              
                Hãy sử dụng thông tin từ context để trả lời câu hỏi.
                Trả lời ngắn gọn, đúng trọng tâm, không dài dòng.
            """
    )
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
