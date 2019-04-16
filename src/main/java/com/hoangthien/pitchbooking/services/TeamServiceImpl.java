package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.District;
import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
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
import java.util.Set;

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
    public Team saveNewTeam(TeamDTO teamDTO) {
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
                .orElseThrow(() -> new PitchBookingException("Khu vực không hợp lệ!"));

        // Update later
        User captain = userRepository
                .findById(1L)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy đội trưởng!"));

        Set<User> members = new HashSet<>();
        members.add(captain);

        Team team = teamMapper.teamDTOToTeam(teamDTO);
        team.setLevel(level);
        team.setArea(area);
        team.setCaptain(captain);
        team.setMembers(members);

        return teamRepository.save(team);
    }

    @Override
    public Team getTeamByPath(String path) {
        return teamRepository
                .findByPath(path)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy team!"));
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
    public long countTotalTeams() {
        return teamRepository.count();
    }

    private boolean isPathExisted(String path) {
        return teamRepository.existsByPath(path);
    }
}
