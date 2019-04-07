package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "group_specific_pitches")
@Data
@NoArgsConstructor
public class GroupSpecificPitches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pitchId")
    private Pitch pitch;

    @ManyToOne
    @JoinColumn(name = "pitchTypeId")
    private PitchType pitchType;

    private int number;
}
