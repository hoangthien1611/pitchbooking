package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "pitch")
@Data
@NoArgsConstructor
public class Pitch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String introduction;

    private String address;

    private String avatar;

    private String detailDescription;

    private String latitude;

    private String longitude;

    private String phoneNumber;

    private String email;

    private String facebook;

    @ManyToOne
    @JoinColumn(name = "yardSurfaceId")
    private YardSurface yardSurface;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private District district;
}
