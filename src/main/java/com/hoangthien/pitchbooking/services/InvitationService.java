package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.InvitationDTO;
import com.hoangthien.pitchbooking.entities.Invitation;

import java.util.List;

public interface InvitationService {

    boolean createInvitation(InvitationDTO invitationDTO, String userName);

    List<Invitation> getAllOfAUser(String userName);

    boolean changeStatus(Long invitationId, int status);

    Invitation getUpcomingMatch(Long teamId);
}
