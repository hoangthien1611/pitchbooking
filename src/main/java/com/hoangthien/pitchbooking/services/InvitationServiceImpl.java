package com.hoangthien.pitchbooking.services;

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
        User user;
        if (invitationDTO.getUserId() == null) {
            user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy user"));
        } else {
            user = userRepository.findById(invitationDTO.getUserId())
                    .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy user"));
        }

        Team team = teamRepository.findById(invitationDTO.getTeamId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy đội khách"));

        Exchange exchange = exchangeRepository.findById(invitationDTO.getExchangeId())
                .orElseThrow(() -> new PitchBookingNotFoundException("Không tìm thấy exchange"));

        if (exchange.getStatus() != 0) {
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
        if (status == 1) {
            Exchange exchange = invitation.getExchange();
            exchange.setStatus(1);
            exchangeRepository.save(exchange);
        }
        return true;
    }
}
