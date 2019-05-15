package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.BookingDTO;
import com.hoangthien.pitchbooking.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookingMapper {

    Booking bookingDTOToBooking(BookingDTO bookingDTO);

    @Mapping(source = "childPitch.name", target = "childPitchName")
    BookingDTO bookingToBookingDTO(Booking booking);
}
