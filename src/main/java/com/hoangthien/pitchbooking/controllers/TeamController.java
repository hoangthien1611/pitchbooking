package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.dto.TeamDTO;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
                         @RequestParam("imgTeam") MultipartFile banner, RedirectAttributes ra, HttpServletRequest request) {
        log.info("POST: " + BASE_URL + "/create");
        if (rs.hasErrors()) {
            return "team/create";
        }

        if (!logo.getOriginalFilename().isEmpty()) {
            teamDTO.setLogo(fileService.saveFile(logo));
        }

        if (!banner.getOriginalFilename().isEmpty()) {
            teamDTO.setPicture(fileService.saveFile(banner));
        }

        teamService.saveNewTeam(teamDTO);

        return "team/create";
    }

}
