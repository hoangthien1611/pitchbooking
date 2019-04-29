package com.hoangthien.pitchbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecificPitchesCostDTO {

    private Long id;

    private String fromTime;

    private String toTime;

    private int cost;

    private Long groupDaysId;

    private String groupDaysName;

    private Long groupSpecificPitchesId;
}
