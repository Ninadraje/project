package com.project.linkedIn.notification_service.respository;

import com.project.linkedIn.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
