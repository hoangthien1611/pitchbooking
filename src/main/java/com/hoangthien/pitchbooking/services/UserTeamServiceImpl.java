package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.entities.UserTeam;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.repositories.TeamRepository;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import com.hoangthien.pitchbooking.repositories.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTeamServiceImpl implements UserTeamService {

    @Autowired
    private UserTeamRepository userTeamRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean save(Long teamId, String userName) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy team"));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy user"));

        UserTeam userTeam = new UserTeam(user, team, false);
        userTeamRepository.save(userTeam);

        return true;
    }

    @Override
    public List<UserTeam> getAllJoinRequests(String userName) {
        return userTeamRepository.findAllByTeamCaptainUserNameAndAcceptedFalse(userName);
    }

    @Override
    public boolean acceptJoinTeam(Long userId, Long teamId) {
        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy yêu cầu"));
        userTeam.setAccepted(true);
        userTeamRepository.save(userTeam);
        return true;
    }

    @Override
    public boolean deleteJoinTeam(Long userId, Long teamId) {
        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy yêu cầu"));
        userTeamRepository.delete(userTeam);
        return true;
    }
}
