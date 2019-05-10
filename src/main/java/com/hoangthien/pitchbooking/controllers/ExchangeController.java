package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.services.DistrictService;
import com.hoangthien.pitchbooking.services.LevelService;
import com.hoangthien.pitchbooking.services.PitchService;
import com.hoangthien.pitchbooking.services.TeamService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/exchange")
@Log4j2
public class ExchangeController extends BaseController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private PitchService pitchService;

    @GetMapping("/waiting")
    public String getListWaiting() {
        log.info("GET: /exchange/waiting");
        return "exchange/list-waiting";
    }

    @GetMapping("/create")
    public String create(Model model, Principal principal, RedirectAttributes ra) {
        log.info("GET: /exchange/create");

        if (principal == null) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng đăng nhập trước!"));
            return "redirect:/";
        }

        model.addAttribute("teams", teamService.getAllTeamsUserIn(principal.getName()));
        model.addAttribute("levels", levelService.getAllLevels());
        model.addAttribute("pitches", pitchService.getAllByDistrict(1L));
        return "exchange/create-exchange";
    }
}
