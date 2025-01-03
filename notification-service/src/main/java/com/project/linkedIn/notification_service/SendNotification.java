package com.project.linkedIn.notification_service;


import com.project.linkedIn.notification_service.entity.Notification;
import com.project.linkedIn.notification_service.respository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotification {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String message) {

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);

        notificationRepository.save(notification);
    }

}
