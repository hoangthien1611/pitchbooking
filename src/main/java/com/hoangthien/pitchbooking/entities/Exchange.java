package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange")
@Data
@NoArgsConstructor
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timeExchange;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    private int hasPitch;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private District district;

    @ManyToOne
    @JoinColumn(name = "pitchId")
    private Pitch pitch;

    private String area;

    private String bet;

    @ManyToOne
    @JoinColumn(name = "levelId")
    private Level level;

    private String invitation;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userCreated;

    private int status;

    @org.springframework.data.annotation.Transient
    public String getTimeString() {
        String minute = timeExchange.getMinute() < 10 ? ("0" + timeExchange.getMinute()) : ("" + timeExchange.getMinute());
        return new StringBuilder()
                .append(timeExchange.getHour())
                .append("h")
                .append(minute)
                .append(" Ngày ")
                .append(timeExchange.getDayOfMonth())
                .append("/")
                .append(timeExchange.getMonthValue())
                .append("/")
                .append(timeExchange.getYear())
                .toString();
    }
}
