/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising;

import com.ndt.AcademicAdvising.enums.UserRole;
import com.ndt.AcademicAdvising.pojo.Major;
import com.ndt.AcademicAdvising.pojo.Subject;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.MajorRepository;
import com.ndt.AcademicAdvising.repositories.SubjectRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author ngodo
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MajorRepository majorRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Value("${cloudinary.image.default}")
    private String imageDefault;

    @Value("${cloudinary.image.ai.default}")
    private String imageAIDefault;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public void run(String... args) throws Exception {
        seedAdminAI();
        seedMajor();
        seedSubject();
    }

    private void seedAdminAI() {
        if (!userRepo.existsByUsername("admin")) {

            User admin = new User();

            admin.setFirstName("Viên");
            admin.setLastName("Quản trị");
            admin.setUsername("admin");
            admin.setPassword(
                    passwordEncoder.encode("123456")
            );
            admin.setEmail("admin@gmail.com");
            admin.setStudentCode("1111111111");
            admin.setAvatar(imageDefault);
            admin.setUserRole(UserRole.ROLE_ADMIN);

            this.userRepo.save(admin);
        }

        if (!userRepo.existsByUsername("aiagent")) {

            User ai = new User();

            ai.setFirstName("Agent");
            ai.setLastName("AI");
            ai.setUsername("aiagent");
            ai.setPassword(
                    passwordEncoder.encode("123456")
            );
            ai.setEmail("aiagent@gmail.com");
            ai.setStudentCode("0000000000");
            ai.setAvatar(imageAIDefault);
            ai.setUserRole(UserRole.ROLE_AI);

            this.userRepo.save(ai);
        }
    }

    private void seedMajor() {
        List<String> majors = List.of(
                "Công nghệ giáo dục",
                "Ngôn ngữ Anh",
                "Ngôn ngữ Trung Quốc",
                "Ngôn ngữ Nhật",
                "Ngôn ngữ Hàn Quốc",
                "Kinh tế",
                "Kinh tế đầu tư",
                "Xã hội học",
                "Tâm lý học",
                "Đông Nam Á học",
                "Truyền thông đa phương tiện",
                "Quản trị kinh doanh",
                "Marketing",
                "Kinh doanh quốc tế",
                "Tài chính - Ngân hàng",
                "Bảo hiểm",
                "Công nghệ tài chính",
                "Kế toán",
                "Kiểm toán",
                "Quản lý công",
                "Quản trị nhân lực",
                "Hệ thống thông tin quản lý",
                "Luật",
                "Luật kinh tế",
                "Công nghệ sinh học",
                "Sinh học ứng dụng",
                "Khoa học dữ liệu",
                "Toán Ứng dụng",
                "Khoa học máy tính",
                "Kỹ thuật phần mềm",
                "Trí tuệ nhân tạo",
                "Công nghệ thông tin",
                "An toàn thông tin",
                "Công nghệ kỹ thuật công trình xây dựng",
                "Logistics và quản lý chuỗi cung ứng",
                "Công nghệ thực phẩm",
                "Kỹ thuật xây dựng",
                "Quản lý xây dựng",
                "Công tác xã hội",
                "Du lịch"
        );

        for (String name : majors) {

            if (!majorRepo.existsByName(name)) {
                Major major = new Major();
                major.setName(name);

                majorRepo.save(major);
            }
        }

        System.out.println("=== MAJOR SEED COMPLETED ===");
    }

    private void seedSubject() {
        Map<String, List<String>> data = new LinkedHashMap<>();

        data.put("Công nghệ giáo dục", List.of(
                "Cơ sở công nghệ giáo dục",
                "Thiết kế học liệu số",
                "Công nghệ dạy học",
                "Đánh giá trong giáo dục"
        ));

        data.put("Ngôn ngữ Anh", List.of(
                "Ngữ âm và âm vị học",
                "Ngữ pháp tiếng Anh",
                "Kỹ năng nghe nói tiếng Anh",
                "Kỹ năng đọc viết tiếng Anh"
        ));

        data.put("Ngôn ngữ Trung Quốc", List.of(
                "Ngữ âm tiếng Trung",
                "Ngữ pháp tiếng Trung",
                "Kỹ năng nghe nói tiếng Trung",
                "Kỹ năng đọc viết tiếng Trung"
        ));

        data.put("Ngôn ngữ Nhật", List.of(
                "Tiếng Nhật sơ cấp",
                "Ngữ pháp tiếng Nhật",
                "Hán tự tiếng Nhật",
                "Kỹ năng giao tiếp tiếng Nhật"
        ));

        data.put("Ngôn ngữ Hàn Quốc", List.of(
                "Tiếng Hàn sơ cấp",
                "Ngữ pháp tiếng Hàn",
                "Hán Hàn",
                "Kỹ năng giao tiếp tiếng Hàn"
        ));

        data.put("Kinh tế", List.of(
                "Kinh tế vi mô",
                "Kinh tế vĩ mô",
                "Kinh tế lượng",
                "Kinh tế phát triển"
        ));

        data.put("Kinh tế đầu tư", List.of(
                "Nguyên lý đầu tư",
                "Phân tích đầu tư",
                "Thị trường chứng khoán",
                "Quản trị danh mục đầu tư"
        ));

        data.put("Xã hội học", List.of(
                "Nhập môn xã hội học",
                "Phương pháp nghiên cứu xã hội học",
                "Xã hội học gia đình",
                "Xã hội học đô thị"
        ));

        data.put("Tâm lý học", List.of(
                "Tâm lý học đại cương",
                "Tâm lý học phát triển",
                "Tâm lý học xã hội",
                "Tâm lý học nhân cách"
        ));

        data.put("Đông Nam Á học", List.of(
                "Lịch sử Đông Nam Á",
                "Văn hóa Đông Nam Á",
                "Kinh tế Đông Nam Á",
                "Chính trị Đông Nam Á"
        ));

        data.put("Truyền thông đa phương tiện", List.of(
                "Cơ sở truyền thông",
                "Thiết kế đồ họa",
                "Sản xuất video",
                "Truyền thông số"
        ));

        data.put("Quản trị kinh doanh", List.of(
                "Quản trị học",
                "Quản trị chiến lược",
                "Quản trị nguồn nhân lực",
                "Quản trị tài chính"
        ));

        data.put("Marketing", List.of(
                "Nguyên lý Marketing",
                "Hành vi người tiêu dùng",
                "Marketing kỹ thuật số",
                "Quản trị thương hiệu"
        ));

        data.put("Kinh doanh quốc tế", List.of(
                "Kinh doanh quốc tế",
                "Thương mại quốc tế",
                "Marketing quốc tế",
                "Quản trị kinh doanh quốc tế"
        ));

        data.put("Tài chính - Ngân hàng", List.of(
                "Tài chính doanh nghiệp",
                "Tiền tệ ngân hàng",
                "Thị trường tài chính",
                "Phân tích tài chính"
        ));

        data.put("Bảo hiểm", List.of(
                "Nguyên lý bảo hiểm",
                "Bảo hiểm tài sản",
                "Bảo hiểm nhân thọ",
                "Quản trị rủi ro bảo hiểm"
        ));

        data.put("Công nghệ tài chính", List.of(
                "Nhập môn FinTech",
                "Thanh toán điện tử",
                "Blockchain và tài chính",
                "Phân tích dữ liệu tài chính"
        ));

        data.put("Kế toán", List.of(
                "Nguyên lý kế toán",
                "Kế toán tài chính",
                "Kế toán quản trị",
                "Kế toán doanh nghiệp"
        ));

        data.put("Kiểm toán", List.of(
                "Nguyên lý kiểm toán",
                "Kiểm toán tài chính",
                "Kiểm toán nội bộ",
                "Kiểm soát nội bộ"
        ));

        data.put("Quản lý công", List.of(
                "Quản lý hành chính công",
                "Chính sách công",
                "Tài chính công",
                "Quản trị khu vực công"
        ));

        data.put("Quản trị nhân lực", List.of(
                "Quản trị nguồn nhân lực",
                "Tuyển dụng nhân sự",
                "Đào tạo và phát triển nhân lực",
                "Đánh giá hiệu quả nhân viên"
        ));

        data.put("Hệ thống thông tin quản lý", List.of(
                "Cơ sở hệ thống thông tin",
                "Phân tích và thiết kế hệ thống",
                "Cơ sở dữ liệu",
                "Hệ thống thông tin doanh nghiệp"
        ));

        data.put("Luật", List.of(
                "Lý luận Nhà nước và pháp luật",
                "Luật dân sự",
                "Luật hình sự",
                "Luật hành chính"
        ));

        data.put("Luật kinh tế", List.of(
                "Pháp luật kinh doanh",
                "Luật doanh nghiệp",
                "Luật thương mại",
                "Pháp luật hợp đồng"
        ));

        data.put("Công nghệ sinh học", List.of(
                "Sinh học phân tử",
                "Vi sinh vật học",
                "Di truyền học",
                "Công nghệ gen"
        ));

        data.put("Sinh học ứng dụng", List.of(
                "Sinh học đại cương",
                "Sinh học thực nghiệm",
                "Sinh học ứng dụng",
                "Phân tích sinh học"
        ));

        data.put("Khoa học dữ liệu", List.of(
                "Lập trình Python",
                "Xác suất thống kê",
                "Khai phá dữ liệu",
                "Học máy"
        ));

        data.put("Toán Ứng dụng", List.of(
                "Giải tích",
                "Đại số tuyến tính",
                "Phương trình vi phân",
                "Toán tối ưu"
        ));

        data.put("Khoa học máy tính", List.of(
                "Lập trình căn bản",
                "Cấu trúc dữ liệu và giải thuật",
                "Hệ điều hành",
                "Cơ sở dữ liệu"
        ));

        data.put("Kỹ thuật phần mềm", List.of(
                "Lập trình hướng đối tượng",
                "Công nghệ phần mềm",
                "Phân tích và thiết kế phần mềm",
                "Kiểm thử phần mềm"
        ));

        data.put("Trí tuệ nhân tạo", List.of(
                "Nhập môn trí tuệ nhân tạo",
                "Học máy",
                "Học sâu",
                "Thị giác máy tính"
        ));

        data.put("Công nghệ thông tin", List.of(
                "Lập trình hướng đối tượng",
                "Cơ sở dữ liệu",
                "Mạng máy tính",
                "Phát triển ứng dụng Web"
        ));

        data.put("An toàn thông tin", List.of(
                "An toàn mạng",
                "Mật mã học",
                "An ninh hệ thống",
                "Kiểm thử an ninh"
        ));

        data.put("Công nghệ kỹ thuật công trình xây dựng", List.of(
                "Cơ học xây dựng",
                "Vật liệu xây dựng",
                "Kết cấu công trình",
                "Kỹ thuật thi công"
        ));

        data.put("Logistics và quản lý chuỗi cung ứng", List.of(
                "Nhập môn Logistics",
                "Quản trị chuỗi cung ứng",
                "Quản trị kho hàng",
                "Vận tải và phân phối"
        ));

        data.put("Công nghệ thực phẩm", List.of(
                "Hóa sinh thực phẩm",
                "Vi sinh thực phẩm",
                "Công nghệ chế biến thực phẩm",
                "An toàn thực phẩm"
        ));

        data.put("Kỹ thuật xây dựng", List.of(
                "Cơ học kết cấu",
                "Vật liệu xây dựng",
                "Kết cấu bê tông cốt thép",
                "Kỹ thuật thi công xây dựng"
        ));

        data.put("Quản lý xây dựng", List.of(
                "Quản lý dự án xây dựng",
                "Kinh tế xây dựng",
                "Lập kế hoạch xây dựng",
                "Quản lý chi phí xây dựng"
        ));

        data.put("Công tác xã hội", List.of(
                "Nhập môn công tác xã hội",
                "Công tác xã hội cá nhân",
                "Công tác xã hội nhóm",
                "Phát triển cộng đồng"
        ));

        data.put("Du lịch", List.of(
                "Tổng quan du lịch",
                "Quản trị lữ hành",
                "Quản trị khách sạn",
                "Marketing du lịch"
        ));

        for (Map.Entry<String, List<String>> entry : data.entrySet()) {

            Major major = majorRepo.findByName(entry.getKey());

            for (String subjectName : entry.getValue()) {

                if (!subjectRepo.existsByNameAndMajorId(
                        subjectName,
                        major.getId())) {

                    Subject subject = new Subject();

                    subject.setName(subjectName);
                    subject.setDescription(
                            "Môn học thuộc ngành " + major.getName()
                    );
                    
                    subject.setMajor(major);

                    subjectRepo.save(subject);
                }
            }
        }

        System.out.println("=== SUBJECT SEED COMPLETED ===");
    }

}
