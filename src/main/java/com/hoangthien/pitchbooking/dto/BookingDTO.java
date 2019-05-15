package com.hoangthien.pitchbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDTO {

    private Long id;

    private String content;

    private String orderName;

    private String orderPhone;

    private String dateBookingString;

    private String fromTime;

    private String toTime;

    private int cost;

    private LocalDateTime timeCreated;

    private Long childPitchId;

    private Long userId;

    private String userName;

    private Long pitchesCostId;

    private String childPitchName;

    private String pitchName;

    private boolean outDate = false;

    private boolean accepted;

    private String pitchAvatar;

    private Long pitchId;

    private String pitchAddress;

    public String getCostCommafy() {
        String regex = "(\\d)(?=(\\d{3})+$)";
        String costString = "" + cost;
        if (costString.length() > 4) {
            costString = costString.replaceAll(regex, "$1,");
        }
        return costString;
    }
}
