package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select distinct n from Notification n join n.user u where u.userName = ?1 order by n.timeCreated desc")
    Page<Notification> getAllByUserAndOrderByTimeCreatedDesc(String userName, Pageable pageable);

    int countByUserUserNameAndIsNewIsTrue(String userName);

    List<Notification> findAllByUserUserName(String userName);
}
