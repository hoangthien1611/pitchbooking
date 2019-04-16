package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.domain.Page;

public interface TeamService {

    Team saveNewTeam(TeamDTO teamDTO);

    Team getTeamByPath(String path);

    Page<Team> getTeamsByAreaAndLevelPageable(Long areaId, Long levelId, int offset);

    Page<Team> getTeamsByAreaPageable(Long areaId, int offset);

    Page<Team> getTeamsByLevelPageable(Long levelId, int offset);

    Page<Team> getAllTeamsPageable(int offset);

    long countTotalTeams();
}
