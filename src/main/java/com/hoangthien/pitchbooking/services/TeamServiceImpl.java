package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.TeamMapper;
import com.hoangthien.pitchbooking.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Override
    public TeamDTO saveNewTeam(TeamDTO teamDTO) {
        if (isPathExisted(teamDTO.getPath())) {
            throw new PitchBookingException("Path đã tồn tại");
        }
        if (teamDTO.getYoungest() > teamDTO.getOldest()) {
            throw new PitchBookingException("Tuổi nhỏ nhất không được lớn hơn tuổi lớn nhất");
        }

        Team team = teamMapper.teamDTOToTeam(teamDTO);

        return teamMapper.teamToTeamDTO(teamRepository.save(team));
    }

    private boolean isPathExisted(String path) {
        return teamRepository.existsByPath(path);
    }
}
