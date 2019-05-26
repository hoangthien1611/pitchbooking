package com.hoangthien.pitchbooking.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_team")
@IdClass(UserTeamId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTeam {

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private Team team;

    private boolean accepted;
}
