package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.InvitationDTO;
import com.hoangthien.pitchbooking.entities.Exchange;
import com.hoangthien.pitchbooking.entities.Invitation;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.repositories.ExchangeRepository;
import com.hoangthien.pitchbooking.repositories.InvitationRepository;
import com.hoangthien.pitchbooking.repositories.TeamRepository;
import com.hoangthien.pitchbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Override
    public boolean createInvitation(InvitationDTO invitationDTO, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy người gửi"));

        Team team = teamRepository.findById(invitationDTO.getTeamSenderId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy đội bóng"));

        Exchange exchange = exchangeRepository.findById(invitationDTO.getExchangeId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy exchange"));

        Invitation invitation = new Invitation();
        invitation.setUserSender(user);
        invitation.setExchange(exchange);
        invitation.setTeamSender(team);
        invitation.setMessage(invitationDTO.getMessage());
        invitation.setStatus(0);
        invitationRepository.save(invitation);

        return true;
    }

    @Override
    public List<Invitation> getAllOfAUser(String userName) {
        return invitationRepository.findAllByUserSenderUserName(userName);
    }
}
