package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.District;
import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.mapper.TeamMapper;
import com.hoangthien.pitchbooking.repositories.DistrictRepository;
import com.hoangthien.pitchbooking.repositories.LevelRepository;
import com.hoangthien.pitchbooking.repositories.TeamRepository;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Override
    public Team saveNewTeam(TeamDTO teamDTO, String userName) {
        if (isPathExisted(teamDTO.getPath().trim())) {
            throw new PitchBookingException("Path đã tồn tại!");
        }
        if (teamDTO.getYoungest() > teamDTO.getOldest()) {
            throw new PitchBookingException("Tuổi nhỏ nhất không được lớn hơn tuổi lớn nhất!");
        }

        Level level = levelRepository
                .findById(teamDTO.getLevelId())
                .orElseThrow(() -> new PitchBookingException("Trình độ không hợp lệ!"));

        District area = districtRepository
                .findById(teamDTO.getAreaId())
                .orElseThrow(() -> new PitchBookingException("Khu vực không tìm thấy!"));

        User captain = userRepository
                .findByUserName(userName)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy đội trưởng!"));

        Team team = teamMapper.teamDTOToTeam(teamDTO);
        team.setLevel(level);
        team.setArea(area);
        team.setCaptain(captain);

        return teamRepository.save(team);
    }

    @Override
    public Team update(TeamDTO teamDTO) {
        if (teamDTO.getYoungest() > teamDTO.getOldest()) {
            throw new PitchBookingException("Tuổi nhỏ nhất không được lớn hơn tuổi lớn nhất!");
        }

        Team team = teamRepository.findById(teamDTO.getId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy team cần update"));

        if (!team.getPath().equalsIgnoreCase(teamDTO.getPath().trim())) {
            if (isPathExisted(teamDTO.getPath().trim())) {
                throw new PitchBookingException("Path đã tồn tại!");
            }
        }

        Level level = levelRepository
                .findById(teamDTO.getLevelId())
                .orElseThrow(() -> new PitchBookingException("Trình độ không hợp lệ!"));

        District area = districtRepository
                .findById(teamDTO.getAreaId())
                .orElseThrow(() -> new PitchBookingException("Khu vực không tìm thấy!"));

        team.setLevel(level);
        team.setArea(area);
        team.setDescription(teamDTO.getDescription());
        team.setName(teamDTO.getName());
        team.setPath(teamDTO.getPath());
        team.setTime(teamDTO.getTime());
        team.setYoungest(teamDTO.getYoungest());
        team.setOldest(teamDTO.getOldest());
        if (teamDTO.getLogo() != null) {
            team.setLogo(teamDTO.getLogo());
        }
        if (teamDTO.getPicture() != null) {
            team.setPicture(teamDTO.getPicture());
        }

        return teamRepository.save(team);
    }

    @Override
    public Team getTeamByPath(String path) {
        return teamRepository
                .findByPath(path)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy team!"));
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy team!"));
    }

    @Override
    public Page<Team> getTeamsByAreaAndLevelPageable(Long areaId, Long levelId, int offset) {
        return teamRepository.findAllByAreaIdAndLevelId(areaId, levelId, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getTeamsByAreaPageable(Long areaId, int offset) {
        return teamRepository.findAllByAreaId(areaId, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getTeamsByLevelPageable(Long levelId, int offset) {
        return teamRepository.findAllByLevelId(levelId, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getAllTeamsPageable(int offset) {
        return teamRepository.findAll(PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getAllTeamsPageable(List<Long> levelIds, String search, int offset) {
        return teamRepository.findAllByLevelIdInAndSearch(levelIds, search.toLowerCase(), PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getAllTeamsPageable(List<Long> levelIds, int offset) {
        return teamRepository.findAllByLevelIdIn(levelIds, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getAllTeamsPageable(Long areaId, List<Long> levelIds, String search, int offset) {
        return teamRepository.findAllByAreaIdAndLevelIdInAndSearch(areaId, levelIds, search.toLowerCase(), PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public Page<Team> getAllTeamsPageable(Long areaId, List<Long> levelIds, int offset) {
        return teamRepository.findAllByAreaIdAndLevelIdIn(areaId, levelIds, PageRequest.of(offset, Defines.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public long countTotalTeams() {
        return teamRepository.count();
    }

    @Override
    public List<Team> get5TeamsSameLevel(Long teamId, Long levelId) {
        Page<Team> teams = teamRepository.findAllByLevelIdAndIdNot(levelId, teamId, PageRequest.of(0, 5));
        return teams.getContent();
    }

    @Override
    public List<Team> getAllTeamsUserIn(String userName) {
        List<Team> teamsByCaptain = teamRepository.findAllByCaptainUserName(userName);
        List<Team> teamsByMember = teamRepository.findAllByMemberUserName(userName);
        teamsByMember.forEach(team -> {
            if (!teamsByCaptain.contains(team)) {
                teamsByCaptain.add(team);
            }
        });
        return teamsByCaptain;
    }

    @Override
    public boolean isPathExisted(String path) {
        return teamRepository.existsByPath(path);
    }

    @Override
    public boolean delete(Long teamId) {
        teamRepository.deleteById(teamId);
        return true;
    }
}
