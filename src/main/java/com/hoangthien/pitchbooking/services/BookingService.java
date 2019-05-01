package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.BookingDTO;

public interface BookingService {

    BookingDTO save(BookingDTO bookingDTO);

    void delete(Long id);
}
