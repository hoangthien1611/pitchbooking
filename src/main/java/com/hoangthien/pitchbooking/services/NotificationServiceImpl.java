package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.entities.Notification;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void create(User user, String name, String content, String link, String icon) {
        Notification notification = new Notification();
        notification.setTimeCreated(LocalDateTime.now());
        notification.setUser(user);
        notification.setName(name);
        notification.setContent(content);
        notification.setLink(link);
        notification.setIcon(icon);

        notificationRepository.save(notification);
    }

    @Override
    public Page<Notification> getNotificationsByUser(String userName, int page) {
        return notificationRepository.getAllByUserAndOrderByTimeCreatedDesc(userName, PageRequest.of(page, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public int countNewNotifications(String userName) {
        return notificationRepository.countByUserUserNameAndIsNewIsTrue(userName);
    }

    @Override
    public boolean resetCountNew(String userName) {
        List<Notification> notifications = notificationRepository.findAllByUserUserName(userName);
        notifications.forEach(notification -> notification.setNew(false));
        if (notifications.size() > 0) {
            notificationRepository.saveAll(notifications);
        }
        return true;
    }

    @Override
    public boolean see(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy thống báo"));
        notification.setSeen(true);
        notificationRepository.save(notification);
        return true;
    }
}
