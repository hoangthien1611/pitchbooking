package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDTO {
    private Long id;

    private String name;

    private String path;

    private String description;

    private String picture;

    private String logo;

    private int youngest;

    private int oldest;

    private String home;

    private String time;

    private Long levelId;

    private Long captainId;

    private Long areaId;
}
