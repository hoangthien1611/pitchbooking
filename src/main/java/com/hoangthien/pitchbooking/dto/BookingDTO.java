package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDTO {

    private Long id;

    private String content;

    private String orderName;

    private String orderPhone;

    private String dateBookingString;

    private String fromTime;

    private String toTime;

    private int cost;

    private LocalDateTime timeCreated;

    private Long childPitchId;

    private Long userId;
}