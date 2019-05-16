package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeamService {

    Team saveNewTeam(TeamDTO teamDTO, String userName);

    Team getTeamByPath(String path);

    Page<Team> getTeamsByAreaAndLevelPageable(Long areaId, Long levelId, int offset);

    Page<Team> getTeamsByAreaPageable(Long areaId, int offset);

    Page<Team> getTeamsByLevelPageable(Long levelId, int offset);

    Page<Team> getAllTeamsPageable(int offset);

    Page<Team> getAllTeamsPageable(List<Long> levelIds, String search, int offset);

    Page<Team> getAllTeamsPageable(List<Long> levelIds, int offset);

    Page<Team> getAllTeamsPageable(Long areaId, List<Long> levelIds, String search, int offset);

    Page<Team> getAllTeamsPageable(Long areaId, List<Long> levelIds, int offset);

    long countTotalTeams();

    List<Team> get5TeamsSameLevel(Long teamId, Long levelId);

    List<Team> getAllTeamsUserIn(String userName);

    boolean isPathExisted(String path);
}
