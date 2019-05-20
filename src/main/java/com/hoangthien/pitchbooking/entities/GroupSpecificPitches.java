package com.hoangthien.pitchbooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.List;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(name = "group_specific_pitches")
@Data
@NoArgsConstructor
public class GroupSpecificPitches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pitchId")
    private Pitch pitch;

    @ManyToOne
    @JoinColumn(name = "pitchTypeId")
    private PitchType pitchType;

    @OnDelete(action = CASCADE)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "groupSpecificPitches",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<ChildPitch> childPitches;

    @OnDelete(action = CASCADE)
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "groupSpecificPitches",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<SpecificPitchesCost> specificPitchesCosts;
}
