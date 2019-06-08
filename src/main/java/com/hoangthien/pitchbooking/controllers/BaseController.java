package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.services.DistrictService;
import com.hoangthien.pitchbooking.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class BaseController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addCommonObjects(Model model, Principal principal) {
        model.addAttribute("districts", districtService.getAllDistrictDTOS());
        if (principal != null) {
            model.addAttribute("notifications", notificationService.getNotificationsByUser(principal.getName(), 0));
            model.addAttribute("newNotifications", notificationService.countNewNotifications(principal.getName()));
        }
    }
}
