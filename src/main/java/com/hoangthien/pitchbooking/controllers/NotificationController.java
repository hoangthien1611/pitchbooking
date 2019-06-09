package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.entities.Notification;
import com.hoangthien.pitchbooking.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/notification")
@Log4j2
public class NotificationController extends BaseController {

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

    @GetMapping("/history")
    public String getAllNotifications(Model model, @RequestParam(value = "p", defaultValue = "1") String pg,
                                      Principal principal) {
        try {
            int page = Integer.parseInt(pg);
            page = page < 1? 0 : (page - 1);
            Page<Notification> pages = notificationService.getNotificationsByUser(principal.getName(), page);

            int totalPages = pages.getTotalPages();
            if (totalPages > 0) {
                int pageEnd = (totalPages < 5) ? totalPages : 5;
                int pageStart = 1;
                if (page > 3) {
                    pageEnd = ((page + 2) < totalPages) ? (page + 2) : totalPages;
                    pageStart = ((pageEnd - 4) < 1) ? 1 : (pageEnd - 4);
                }

                model.addAttribute("pageStart", pageStart);
                model.addAttribute("pageEnd", pageEnd);
                model.addAttribute("currentPage", (page+1));
                model.addAttribute("totalPages", totalPages);
                model.addAttribute("listNotifications", pages.getContent());
            }
            return "profile/notifications";

        } catch (Exception e) {
            log.error(e.getMessage());
            return "error/page_500";
        }
    }
}
