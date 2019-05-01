package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.BookingDTO;
import com.hoangthien.pitchbooking.entities.Booking;
import org.mapstruct.Mapper;

@Mapper
public interface BookingMapper {

    Booking bookingDTOToBooking(BookingDTO bookingDTO);

    BookingDTO bookingToBookingDTO(Booking booking);
}
