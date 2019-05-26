package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.UserTeam;

import java.util.List;

public interface UserTeamService {

    boolean save(Long teamId, String userName);

    List<UserTeam> getAllJoinRequests(String userName);

    boolean acceptJoinTeam(Long userId, Long teamId);

    boolean deleteJoinTeam(Long userId, Long teamId);
}
