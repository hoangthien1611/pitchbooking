package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.services.DistrictService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exchange")
@Log4j2
public class ExchangeController extends BaseController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("/waiting")
    public String getListWaiting() {
        log.info("GET: /exchange/waiting");
        return "exchange/list-waiting";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("GET: /exchange/create");
        return "exchange/create-exchange";
    }
}
