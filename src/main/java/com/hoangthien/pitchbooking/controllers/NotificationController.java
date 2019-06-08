package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/notification")
@Log4j2
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PutMapping("/resetNew")
    @ResponseBody
    public boolean resetNew(Principal principal) {
        try {
            return notificationService.resetCountNew(principal.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @PutMapping("/see/{id}")
    @ResponseBody
    public boolean see(@PathVariable("id") Long id) {
        try {
            return notificationService.see(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
