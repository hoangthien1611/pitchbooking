package com.hoangthien.pitchbooking.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exchange")
@Log4j2
public class ExchangeController extends BaseController {

    @GetMapping("/waiting")
    public String getListWaiting() {
        log.info("GET: /exchange/waiting");
        return "exchange/list-waiting";
    }
}
