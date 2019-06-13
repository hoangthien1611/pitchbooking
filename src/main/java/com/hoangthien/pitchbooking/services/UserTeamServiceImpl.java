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

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean save(Long teamId, String userName) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy team"));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy user"));

        UserTeam userTeam = new UserTeam(user, team, false);
        userTeamRepository.save(userTeam);

        notificationService.create(team.getCaptain(), user.getFullName(),
                "đã gửi yêu cầu tham gia đội bóng " + team.getName(), "/team/my-teams/join-requests", "add-member.png");

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

        String link = "/team/detail/" + userTeam.getTeam().getPath();

        notificationService.create(userTeam.getUser(), userTeam.getTeam().getCaptain().getFullName(),
                "đã chấp nhận yêu cầu tham gia đội bóng của bạn", link, "add-member.png");

        return true;
    }

    @Override
    public boolean deleteJoinTeam(Long userId, Long teamId) {
        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy yêu cầu"));
        userTeamRepository.delete(userTeam);

        String link = "/team/detail/" + userTeam.getTeam().getPath();

        notificationService.create(userTeam.getUser(), userTeam.getTeam().getCaptain().getFullName(),
                "đã từ chối yêu cầu tham gia đội bóng của bạn", link, "add-member.png");

        return true;
    }
}
