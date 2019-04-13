package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.Team;

import java.util.List;

public interface TeamService {

    Team saveNewTeam(TeamDTO teamDTO);

    Team getTeamByPath(String path);

    List<Team> getTeamsByAreaAndLevel(Long areaId, Long levelId, int offset);

    List<Team> getTeamsByArea(Long areaId, int offset);

//    int getNumberOfTeams(Long areaId, Long levelId);
}
