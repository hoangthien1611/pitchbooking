package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "pitch")
@Data
@NoArgsConstructor
public class Pitch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1024)
    private String introduction;

    private String address;

    private String avatar;

    @Lob
    private String detailDescription;

    private String latitude;

    private String longitude;

    private String phoneNumber;

    private String email;

    private String facebook;

    @ManyToOne
    @JoinColumn(name = "yardSurfaceId")
    @ToString.Exclude
    private YardSurface yardSurface;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private District district;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "pitch",
            cascade = CascadeType.ALL)
    private List<GroupSpecificPitches> groupSpecificPitches;
}
