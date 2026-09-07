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
public interface ChatRAGAssistant {
    @SystemMessage(
        """
                Bạn là trợ lý tư vấn học vụ của Trường Đại học Mở
                Thành phố Hồ Chí Minh.

                Chỉ sử dụng thông tin trong context.
                Không tự suy đoán hoặc bịa thông tin.
                Nếu không đủ thông tin, hãy nói rõ không tìm thấy
                thông tin phù hợp trong dữ liệu hiện có.

                Trả lời ngắn gọn, đúng trọng tâm.
        """
    )
    String chat(@MemoryId int conversationId, @UserMessage String userMessage);
}
