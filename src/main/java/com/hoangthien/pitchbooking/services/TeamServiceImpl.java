package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Define;
import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.*;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.TeamMapper;
import com.hoangthien.pitchbooking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
    public List<Team> getTeamsByAreaAndLevel(Long areaId, Long levelId, int offset) {
        return teamRepository.findAllByAreaIdAndLevelId(areaId, levelId, PageRequest.of(offset, Define.NUMBER_OF_ROWS_PER_PAGE));
    }

    @Override
    public List<Team> getTeamsByArea(Long areaId, int offset) {
        return teamRepository.findAllByAreaId(areaId, PageRequest.of(offset, Define.NUMBER_OF_ROWS_PER_PAGE));
    }

    private boolean isPathExisted(String path) {
        return teamRepository.existsByPath(path);
    }
}
