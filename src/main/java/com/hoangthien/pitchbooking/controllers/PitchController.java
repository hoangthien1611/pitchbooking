package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.UserDTO;
import com.hoangthien.pitchbooking.services.DistrictService;
import com.hoangthien.pitchbooking.services.YardSurfaceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(PitchController.BASE_URL)
@Log4j2
public class PitchController {

    public static final String BASE_URL = "/pitch";

    @Autowired
    private YardSurfaceService yardSurfaceService;

    @Autowired
    private DistrictService districtService;

    @GetMapping("/create")
    public String create(Model model) {
        log.info("GET: " + BASE_URL + "/create");
        model.addAttribute("listDistricts", districtService.getAllDistricts());
        model.addAttribute("listSurfaces", yardSurfaceService.getAllYardSurfaces());
        return "pitch/create";
    }
}
