package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "child_pitch")
@Data
@NoArgsConstructor
public class ChildPitch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "groupSpecificPitchesId")
    private GroupSpecificPitches groupSpecificPitches;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "childPitch",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
