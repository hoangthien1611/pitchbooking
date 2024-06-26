package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeamService {

    Team saveNewTeam(TeamDTO teamDTO, String userName);

    Team update(TeamDTO teamDTO);

    Team getTeamByPath(String path);

    Team getTeamById(Long id);

    Page<Team> getTeamsByAreaAndLevelPageable(Long areaId, Long levelId, int offset);

    Page<Team> getTeamsByAreaPageable(Long areaId, int offset);

    Page<Team> getTeamsByLevelPageable(Long levelId, int offset);

    Page<Team> getAllTeamsPageable(int offset);

    Page<Team> getAllTeamsPageable(List<Long> levelIds, String search, int page);

    Page<Team> getAllTeamsPageable(List<Long> levelIds, int page);

    Page<Team> getAllTeamsPageable(Long areaId, List<Long> levelIds, String search, int page);

    Page<Team> getAllTeamsPageable(Long areaId, List<Long> levelIds, int page);

    long countTotalTeams();

    List<Team> get5TeamsSameLevel(Long teamId, Long levelId);

    List<Team> getAllTeamsUserIn(String userName);

    boolean isPathExisted(String path);

    boolean delete(Long teamId);

    boolean joinTeam(Long teamId, String userName);

    boolean isUserOwningTeam(String userName);
}
