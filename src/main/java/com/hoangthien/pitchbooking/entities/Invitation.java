package com.hoangthien.pitchbooking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "invitation")
@Data
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exchangeId")
    private Exchange exchange;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userSender;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team teamSender;

    private String message;

    private int status;
}
