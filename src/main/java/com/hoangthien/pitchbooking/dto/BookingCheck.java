package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookingCheck {

    private TimeFrame timeFrame;

    private LocalDate dateBooking;

    private boolean available;
}
