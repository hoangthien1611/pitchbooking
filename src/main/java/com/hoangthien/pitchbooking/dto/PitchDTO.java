package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PitchDTO {

    private Long id;

    private String name;

    private String introduction;

    private String address;

    private String avatar;

    private String detailDescription;

    private String latitude;

    private String longitude;

    private String phoneNumber;

    private String email;

    private String facebook;

    private long yardSurfaceId;

    private long districtId;
}
