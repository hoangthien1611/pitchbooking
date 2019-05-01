package com.hoangthien.pitchbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrame {

    private String fromTime;

    private String toTime;
}
