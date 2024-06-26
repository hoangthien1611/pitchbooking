package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvitationDTO {

    private Long id;

    private Long exchangeId;

    private Long teamId;

    private Long userId;

    private String message;

    private int type;

    private int status;
}
