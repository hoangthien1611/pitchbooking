package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.dto.TimeFrameBooking;
import com.hoangthien.pitchbooking.entities.Pitch;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

public interface PitchService {

    Pitch saveNewPitch(PitchDTO pitchDTO);

    List<Pitch> getPitchesByOwnerId(long ownerId);

    Pitch getPitchById(long id);

    List<PitchDTO> getAllByDistrict(Long districtId);

    Pitch updatePitch(PitchDTO pitchDTO);

    List<TimeFrameBooking> getTimeFrameBookingsByDate(Long pitchId, LocalDate dateBooking);

    Page<Pitch> getAllPageable(String path, List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, int offset);

    Page<Pitch> getAllPageable(String path, List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, String search, int offset);

    Page<Pitch> getAllPageable(String path, List<Long> typeIds, List<Long> surfaceIds, int offset);

    Page<Pitch> getAllPageable(String path, List<Long> typeIds, List<Long> surfaceIds, String search, int offset);

    Page<Pitch> getAllPageable(List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, int offset);

    Page<Pitch> getAllPageable(List<Integer> costs, List<Long> typeIds, List<Long> surfaceIds, String search, int offset);
}
