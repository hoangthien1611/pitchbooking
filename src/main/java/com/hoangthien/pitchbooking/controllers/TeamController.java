package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.Define;
import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.services.DistrictService;
import com.hoangthien.pitchbooking.services.FileService;
import com.hoangthien.pitchbooking.services.LevelService;
import com.hoangthien.pitchbooking.services.TeamService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(TeamController.BASE_URL)
@Log4j2
public class TeamController {

    public static final String BASE_URL = "/team";

    @Autowired
    private DistrictService districtService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TeamService teamService;

    @GetMapping("/create")
    public String create(Model model) {
        log.info("GET: " + BASE_URL + "/create");
        model.addAttribute("listDistricts", districtService.getAllDistricts());
        model.addAttribute("listLevels", levelService.getAllLevels());
        return "team/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TeamDTO teamDTO, BindingResult rs, @RequestParam("imgLogo") MultipartFile logo,
                         @RequestParam("imgTeam") MultipartFile banner, RedirectAttributes ra) {
        log.info("POST: " + BASE_URL + "/create");
        if (rs.hasErrors()) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng nhập đầy đủ thông tin phù hợp!"));
            return "redirect:/team/create";
        }

        try {
            if (!logo.getOriginalFilename().isEmpty()) {
                teamDTO.setLogo(fileService.saveFile(logo));
            }

            if (!banner.getOriginalFilename().isEmpty()) {
                teamDTO.setPicture(fileService.saveFile(banner));
            }

            teamService.saveNewTeam(teamDTO);
        } catch (Exception e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
            return "redirect:/team/create";
        }

        ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Thêm đội bóng mới thành công"));
        return "redirect:/team/detail/" + teamDTO.getPath();
    }

    @GetMapping("/detail/{path}")
    public String detail(Model model, @PathVariable("path") String path, @RequestParam(value = "tab", defaultValue = "1") String tabStr) {
        log.info("GET: " + BASE_URL + "/detail/" + path);
        try {
            int tab = Integer.parseInt(tabStr);
            if (tab < 1 || tab > 4) {
                throw new PitchBookingException("Tab không hợp lệ!");
            }
            model.addAttribute("team", teamService.getTeamByPath(path.trim()));
            model.addAttribute("tab", tab);
            return "team/detail";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "error/page_404";
    }

    @GetMapping("/list")
    public String getTeams(Model model, @RequestParam(value = "area", defaultValue = "1") String area,
                           @RequestParam(value = "level", defaultValue = "0") String level,
                           @RequestParam(value = "page", defaultValue = "1") String pg) {
        log.info("GET: " + BASE_URL + "/list");
        List<Team> teams = new ArrayList<>();
        int page = 1;

        try {
            Long areaId = Long.valueOf(Integer.parseInt(area));
            Long levelId = Long.valueOf(Integer.parseInt(level));
            page = Integer.parseInt(pg);
            int offset = (page - 1) * Define.NUMBER_OF_ROWS_PER_PAGE;

            if (levelId == 0) {
                teams = teamService.getTeamsByArea(areaId, offset);
            } else {
                teams = teamService.getTeamsByAreaAndLevel(areaId, levelId, offset);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        int sumTeams = teams.size();
        int sumPages = (int) Math.ceil((double) sumTeams / Define.NUMBER_OF_ROWS_PER_PAGE);
        int pageEnd = (sumPages < 5) ? sumPages : 5;
        int pageStart = 1;
        if (page > 3) {
            pageEnd = ((page + 2) < sumPages) ? (page + 2) : sumPages;
            pageStart = ((pageEnd - 4) < 1) ? 1 : (pageEnd - 4);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("sumPages", sumPages);
        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);

        model.addAttribute("teams", teams);
        return "team/list";
    }
}
