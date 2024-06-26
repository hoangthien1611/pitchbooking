package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.BookingDTO;
import com.hoangthien.pitchbooking.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookingMapper {

    Booking bookingDTOToBooking(BookingDTO bookingDTO);

    @Mapping(source = "childPitch.name", target = "childPitchName")
    @Mapping(source = "childPitch.groupSpecificPitches.pitch.avatar", target = "pitchAvatar")
    @Mapping(source = "childPitch.groupSpecificPitches.pitch.id", target = "pitchId")
    @Mapping(source = "childPitch.groupSpecificPitches.pitch.address", target = "pitchAddress")
    BookingDTO bookingToBookingDTO(Booking booking);
}
