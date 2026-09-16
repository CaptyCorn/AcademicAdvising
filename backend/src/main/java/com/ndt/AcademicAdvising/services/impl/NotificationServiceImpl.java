/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.services.impl;

import com.ndt.AcademicAdvising.dto.PageResponseDTO;
import com.ndt.AcademicAdvising.dto.ResponseNotificationDTO;
import com.ndt.AcademicAdvising.enums.NotificationType;
import com.ndt.AcademicAdvising.pojo.Notification;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.NotificationRepository;
import com.ndt.AcademicAdvising.repositories.UserRepository;
import com.ndt.AcademicAdvising.services.NotificationService;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepo;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private UserRepository userRepo;
    
    @Value("${pagination.notification.size}")
    private Integer notificationSize;
    
    private ResponseNotificationDTO toDTO(Notification n) {
        ResponseNotificationDTO dto = new ResponseNotificationDTO();

        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setContent(n.getContent());
        dto.setType(n.getType());
        dto.setIsRead(n.getIsRead());
        dto.setLink(n.getLink());
        dto.setCreatedAt(n.getCreatedAt());

        return dto;
    }
    
    private User getCurrentUser() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userRepo.findByUsername(currentUser);
    }
    
    private PageResponseDTO<ResponseNotificationDTO> toPageDTO(Page<ResponseNotificationDTO> page) {
        PageResponseDTO<ResponseNotificationDTO> dto = new PageResponseDTO<>();
        dto.setContent(page.getContent());
        dto.setPage(page.getNumber());
        dto.setSize(page.getSize());
        dto.setTotalElements(page.getTotalElements());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }

    @Override
    public ResponseNotificationDTO createCommentNotification(User sender, User receiver, int postId) {
        if (Objects.equals(sender.getId(), receiver.getId())) return null;
        
        Notification notification = new Notification();

        notification.setTitle("Có bình luận mới");
        notification.setContent(
                sender.getName() + " đã bình luận vào bài đăng của bạn."
        );
        notification.setType(NotificationType.COMMENT);
        notification.setIsRead(false);
        notification.setLink("/posts/" + postId);
        notification.setSender(sender);
        notification.setReceiver(receiver);

        ResponseNotificationDTO responseNotification = toDTO(this.notificationRepo.save(notification));
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/notifications",
                responseNotification
        );
        return responseNotification;
    }

    @Override
    public PageResponseDTO<ResponseNotificationDTO> getListNotification(Map<String, String> param) {
        int page = 0;
        if (param != null && param.get("page") != null) {
            page = Integer.parseInt(param.get("page"));
        }
        User current = getCurrentUser();
        Pageable pageable = PageRequest.of(page, notificationSize);
        return toPageDTO(
                this.notificationRepo.findAllByReceiverIdOrderByCreatedAtDesc(
                        current.getId(), 
                        pageable).map(this::toDTO)
        );
    }
    
}
