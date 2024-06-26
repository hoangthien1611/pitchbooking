package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "specific_pitches_cost")
@Data
@NoArgsConstructor
public class SpecificPitchesCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "groupDaysId")
    private GroupDays groupDays;

    private String fromTime;

    private String toTime;

    private int cost;

    @ManyToOne
    @JoinColumn(name = "groupSpecificPitchesId")
    private GroupSpecificPitches groupSpecificPitches;

    public String getCostCommafy() {
        String regex = "(\\d)(?=(\\d{3})+$)";
        String costString = "" + cost;
        if (costString.length() > 4) {
            costString = costString.replaceAll(regex, "$1,");
        }
        return costString;
    }
}
