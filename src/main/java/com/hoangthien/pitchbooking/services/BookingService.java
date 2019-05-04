package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.BookingCheck;
import com.hoangthien.pitchbooking.dto.BookingDTO;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO save(BookingDTO bookingDTO);

    void delete(Long id);

    List<BookingCheck> getBookingCheckList(Long pitchesCostId, LocalDate date);
}
