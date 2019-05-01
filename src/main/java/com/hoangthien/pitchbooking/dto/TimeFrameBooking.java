package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TimeFrameBooking {

    private TimeFrame timeFrame;

    private LocalDate dateBooking;

    private List<ChildPitchDTO> childPitches;
}
