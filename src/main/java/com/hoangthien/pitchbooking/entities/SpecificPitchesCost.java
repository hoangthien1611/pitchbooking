package com.hoangthien.pitchbooking.entities;

import javax.persistence.*;

@Entity
@Table(name = "specific_pitches_cost")
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
}
