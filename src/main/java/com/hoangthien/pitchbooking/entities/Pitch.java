package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

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

    @OnDelete(action = CASCADE)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "pitch",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GroupSpecificPitches> groupSpecificPitches;
}
