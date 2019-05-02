package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DistrictDTO {

    private Long id;

    private String name;

    private String path;

    private int totalPitches;
}
