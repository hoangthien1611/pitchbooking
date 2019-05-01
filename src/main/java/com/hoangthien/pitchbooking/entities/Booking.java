package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String orderName;

    private String orderPhone;

    private LocalDate dateBooking;

    private String fromTime;

    private String toTime;

    private boolean status;

    private LocalDateTime timeCreated;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userBooking;

    @ManyToOne
    @JoinColumn(name = "childPitchId")
    private ChildPitch childPitch;
}
