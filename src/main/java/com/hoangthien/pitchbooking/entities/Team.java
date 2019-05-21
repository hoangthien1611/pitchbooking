package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "team")
@Data
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    @Column(length = 2048)
    private String description;

    private String picture;

    private String logo;

    private int youngest;

    private int oldest;

    private String time;

    @ManyToOne
    @JoinColumn(name = "levelId")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User captain;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private District area;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "team_user",
            joinColumns = {@JoinColumn(name = "teamId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")})
    @EqualsAndHashCode.Exclude
    private Set<User> members = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "team",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Exchange> exchanges;
}
