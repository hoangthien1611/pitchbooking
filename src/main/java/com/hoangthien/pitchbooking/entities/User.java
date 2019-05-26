package com.hoangthien.pitchbooking.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String avatar;

    private String userName;

    private String password;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "owner",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Pitch> owningPitches;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "captain",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Team> owningTeams;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<UserTeam> userTeams = new HashSet<>();
}
