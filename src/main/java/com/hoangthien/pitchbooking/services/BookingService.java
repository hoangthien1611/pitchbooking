package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.BookingCheck;
import com.hoangthien.pitchbooking.dto.BookingDTO;
import com.hoangthien.pitchbooking.entities.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO save(BookingDTO bookingDTO);

    void delete(Long id);

    List<BookingCheck> getBookingCheckList(Long pitchesCostId, LocalDate date);

    BookingDTO saveForUser(BookingDTO bookingDTO);

    List<BookingDTO> getAllByUserNameAndNotAccepted(String userName);
}
