package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.ExchangeDTO;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.entities.Exchange;
import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.services.*;
import com.hoangthien.pitchbooking.utils.PitchBookingUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/waiting/{path}")
    public String getListWaiting(Model model, @PathVariable("path") String path,
                                 @RequestParam(value = "p", defaultValue = "1") String pg,
                                 @RequestParam(value = "l", defaultValue = "") String lValue,
                                 @RequestParam(value = "h", defaultValue = "") String hPValue,
                                 @RequestParam(value = "s", defaultValue = "") String search) {
        log.info("GET: /exchange/waiting/" + path);

        try {
            int page = Integer.parseInt(pg);
            int offset = (page - 1) * Defines.NUMBER_OF_ROWS_PER_PAGE;
            Page<Exchange> pageExchanges;
            List<Level> levelList = levelService.getAllLevelsByExchange(path, search);
            List<Long> levelIds = StringUtils.isEmpty(lValue) ? PitchBookingUtils.getIdsFromLevels(levelList)
                    : PitchBookingUtils.convertFromStringListToLongList(lValue);
            List<Integer> hasPitchList = StringUtils.isEmpty(hPValue) ? Arrays.asList(0, 1) : PitchBookingUtils.convertFromStringListToIntList(hPValue);

            if (StringUtils.isEmpty(search)) {
                pageExchanges = exchangeService.getAllPageable(path, hasPitchList, levelIds, offset);
            } else {
                pageExchanges = exchangeService.getAllPageable(path, hasPitchList, levelIds, search, offset);
            }

            int totalPages = pageExchanges.getTotalPages();
            if (totalPages > 0) {
                int pageEnd = (totalPages < 5) ? totalPages : 5;
                int pageStart = 1;
                if (page > 3) {
                    pageEnd = ((page + 2) <= totalPages) ? (page + 2) : totalPages;
                    pageStart = ((pageEnd - 4) < 1) ? 1 : (pageEnd - 4);
                }
                model.addAttribute("pageStart", pageStart);
                model.addAttribute("pageEnd", pageEnd);
            }

            String districtName = Defines.DISTRICT_PATH_ALL.equalsIgnoreCase(path) ? ""
                    : districtService.getDistrictDTOByPath(path).getName();

            model.addAttribute("exchanges", pageExchanges.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("lValue", lValue);
            model.addAttribute("hPValue", hPValue);
            model.addAttribute("search", search);
            model.addAttribute("path", path);
            model.addAttribute("levelList", levelList);
            model.addAttribute("districtName", districtName);
            model.addAttribute("totalExchangesFound", pageExchanges.getTotalElements());

            return "exchange/list-waiting";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
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
    public String create(@ModelAttribute ExchangeDTO exchangeDTO, RedirectAttributes ra, Principal principal) {
        log.info("POST: /exchange/create");
        if (exchangeDTO.getTeamId() == 0) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng chọn đội bóng!"));
            return "redirect:/exchange/create";
        }
        if (exchangeDTO.getHasPitch() == 1 && exchangeDTO.getPitchId() == 0) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng chọn sân hoặc thay đổi lại loại hình!"));
            return "redirect:/exchange/create";
        }
        try {
            exchangeDTO.setUsername(principal.getName());
            if (exchangeService.save(exchangeDTO)) {
                ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Tạo thành công"));
            }
        } catch (PitchBookingException e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
        }
        return "redirect:/exchange/create";
    }
}
