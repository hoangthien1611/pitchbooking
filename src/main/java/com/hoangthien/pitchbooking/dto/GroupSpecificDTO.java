package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupSpecificDTO {

    private Long id;

    private Long pitchId;

    private Long pitchTypeId;

    private int number;

    private String pitchTypeName;
}
