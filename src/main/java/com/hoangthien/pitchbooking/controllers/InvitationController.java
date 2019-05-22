package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.dto.InvitationDTO;
import com.hoangthien.pitchbooking.services.InvitationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/invitation")
@Log4j2
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping
    @ResponseBody
    public boolean invite(@ModelAttribute InvitationDTO invitationDTO, Principal principal) {
        log.info(invitationDTO);
        try {
            return invitationService.createInvitation(invitationDTO, principal.getName());
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }
}
