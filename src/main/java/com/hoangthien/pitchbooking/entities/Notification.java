package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String content;

    private LocalDateTime timeCreated;

    private boolean seen = false;

    private boolean isNew = true;

    private String link;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @org.springframework.data.annotation.Transient
    public String getTimeString() {
        String minute = timeCreated.getMinute() < 10 ? ("0" + timeCreated.getMinute()) : ("" + timeCreated.getMinute());
        return new StringBuilder()
                .append(timeCreated.getHour())
                .append(":")
                .append(minute)
                .append(" ngày ")
                .append(timeCreated.getDayOfMonth())
                .append("/")
                .append(timeCreated.getMonthValue())
                .append("/")
                .append(timeCreated.getYear())
                .toString();
    }
}
