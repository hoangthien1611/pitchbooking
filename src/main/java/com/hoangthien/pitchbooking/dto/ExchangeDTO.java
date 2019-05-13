package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExchangeDTO {

    private Long id;

    private String date;

    private String time;

    private Long teamId;

    private int hasPitch;

    private Long districtId;

    private Long pitchId;

    private String area;

    private String bet;

    private Long levelId;

    private String invitation;

    private String username;
}
