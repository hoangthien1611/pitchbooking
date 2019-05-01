package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.dto.TimeFrameBooking;
import com.hoangthien.pitchbooking.entities.Pitch;

import java.time.LocalDate;
import java.util.List;

public interface PitchService {

    Pitch saveNewPitch(PitchDTO pitchDTO);

    List<Pitch> getPitchesByOwnerId(long ownerId);

    Pitch getPitchById(long id);

    Pitch updatePitch(PitchDTO pitchDTO);

    List<TimeFrameBooking> getTimeFrameBookingsByDate(Long pitchId, LocalDate dateBooking);
}
