package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.ExchangeDTO;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

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

    @Autowired
    private ExchangeService exchangeService;

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
        model.addAttribute("dateDefault", LocalDate.now().plusDays(1));
        return "exchange/create-exchange";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ExchangeDTO exchangeDTO, RedirectAttributes ra) {
        log.info("POST: /exchange/create");
        log.info(exchangeDTO);
        if (exchangeDTO.getTeamId() == 0) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng chọn đội bóng!"));
            return "redirect:/exchange/create";
        }
        if (exchangeDTO.getHasPitch() == 1 && exchangeDTO.getPitchId() == 0) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng chọn sân hoặc thay đổi lại loại hình!"));
            return "redirect:/exchange/create";
        }
        try {
            if (exchangeService.save(exchangeDTO)) {
                ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Tạo thành công"));
            }
        } catch (PitchBookingException e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
        }
        return "redirect:/exchange/create";
    }
}
