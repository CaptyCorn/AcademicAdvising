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
                Bạn là trợ lý chuyên giải đáp các thắc mắc
                về đào tạo và tuyển sinh tại Trường Đại học Mở
                Thành phố Hồ Chí Minh.

                Hãy sử dụng thông tin từ context để trả lời câu hỏi.

                Chỉ sử dụng thông tin có trong context.
                Không tự suy đoán hoặc bịa thông tin.

                Nếu context không chứa đủ thông tin để trả lời,
                hãy nói rõ rằng không tìm thấy thông tin phù hợp
                trong dữ liệu hiện có.

                Trả lời ngắn gọn, đúng trọng tâm, không dài dòng.
            """
    )
    String chat(@UserMessage String userMessage);
}
