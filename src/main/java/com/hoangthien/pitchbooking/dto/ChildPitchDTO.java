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

    private boolean bookingAccepted;

    public String getCostCommafy() {
        String regex = "(\\d)(?=(\\d{3})+$)";
        String costString = "" + cost;
        if (costString.length() > 4) {
            costString = costString.replaceAll(regex, "$1,");
        }
        return costString;
    }
}
