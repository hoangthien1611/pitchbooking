package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.Notification;
import com.hoangthien.pitchbooking.entities.User;
import org.springframework.data.domain.Page;

public interface NotificationService {

    void create(User user, String name, String content, String link, String icon);

    Page<Notification> getNotificationsByUser(String userName, int page);

    int countNewNotifications(String userName);

    boolean resetCountNew(String userName);

    boolean see(Long id);
}
