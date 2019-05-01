package com.hoangthien.pitchbooking.dto;

import com.hoangthien.pitchbooking.entities.Booking;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChildPitchDTO {

    private Long id;

    private String name;

    private int cost;

    private Booking booking;
}
