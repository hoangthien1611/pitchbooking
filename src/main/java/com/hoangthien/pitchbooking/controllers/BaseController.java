package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.services.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BaseController {

    @Autowired
    private DistrictService districtService;

    @ModelAttribute
    public void addCommonObjects(Model model) {
        model.addAttribute("districts", districtService.getAllDistrictDTOS());
    }
}
