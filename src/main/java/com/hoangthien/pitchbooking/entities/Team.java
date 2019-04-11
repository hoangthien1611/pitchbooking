package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String description;

    private String picture;

    private String logo;

    private int youngest;

    private int oldest;

    private String home;

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
}
