package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.InvitationDTO;
import com.hoangthien.pitchbooking.entities.Exchange;
import com.hoangthien.pitchbooking.entities.Invitation;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.repositories.ExchangeRepository;
import com.hoangthien.pitchbooking.repositories.InvitationRepository;
import com.hoangthien.pitchbooking.repositories.TeamRepository;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final String LINK = "/user/invitation-history";
    private final String ICON = "envelope.png";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean createInvitation(InvitationDTO invitationDTO, String userName) {
        User user;
        if (invitationDTO.getUserId() == null) {
            // bắt đối
            user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy user"));
        } else {
            // mời giao lưu
            user = userRepository.findById(invitationDTO.getUserId())
                    .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy user"));
        }

        Team team = teamRepository.findById(invitationDTO.getTeamId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy đội khách"));

        Exchange exchange = exchangeRepository.findById(invitationDTO.getExchangeId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy exchange"));

        if (exchange.getStatus() == 1) {
            throw new PitchBookingException("Trận đấu này đã tìm được đối");
        }

        Invitation invitation = new Invitation();
        invitation.setUser(user);
        invitation.setExchange(exchange);
        invitation.setTeam(team);
        invitation.setMessage(invitationDTO.getMessage());
        invitation.setStatus(0);
        invitation.setType(invitationDTO.getType());
        invitationRepository.save(invitation);

        if (invitationDTO.getType() == 1) {
            notificationService.create(exchange.getUserCreated(), user.getFullName(),
                    "muốn giao lưu trận đấu bạn đã tạo", LINK, ICON);
        } else {
            notificationService.create(invitation.getUser(), exchange.getUserCreated().getFullName(),
                    "đã gửi lời mời giao lưu", LINK, ICON);
        }

        return true;
    }

    @Override
    public List<Invitation> getAllOfAUser(String userName) {
        return invitationRepository.findAllByUserUserName(userName);
    }

    @Override
    public boolean changeStatus(Long invitationId, int status) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy lời mời!"));

        invitation.setStatus(status);
        invitationRepository.save(invitation);
        String content = status == 1? "đã chấp nhận lời mời giao lưu của bạn" : "đã từ chối lời mời giao lưu của bạn";
        User userGetNoti = invitation.getType() == 1? invitation.getUser() : invitation.getExchange().getUserCreated();
        String name = invitation.getType() == 1? invitation.getExchange().getUserCreated().getFullName() : invitation.getUser().getFullName();

        if (status == 1) {
            Exchange exchange = invitation.getExchange();
            exchange.setStatus(1);
            exchangeRepository.save(exchange);
        }

        notificationService.create(userGetNoti, name, content, LINK, ICON);

        return true;
    }

    @Override
    public Invitation getUpcomingMatch(Long teamId) {
        LocalDateTime now = LocalDateTime.now();
        List<Invitation> invitations = invitationRepository
                .findByTeamParticipate(teamId, now, PageRequest.of(0, 1)).getContent();
        if (!CollectionUtils.isEmpty(invitations)) {
            return invitations.get(0);
        }
        return null;
    }
}
