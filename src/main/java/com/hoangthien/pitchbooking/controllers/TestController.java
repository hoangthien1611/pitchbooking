package com.hoangthien.pitchbooking.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/index")
    public String index() {
        return "home/index";
    }

    @GetMapping("/detail")
    public String detail() {
        return "pitches/pitch-detail";
    }

    @GetMapping("/list")
    public String layout() {
        return "pitches/pitches";
    }

    @GetMapping("/create-exchange")
    public String createExchange() {
        return "exchange/create-exchange";
    }

    @GetMapping("/list-team")
    public String listTeam() {
        return "exchange/list-team";
    }

    @GetMapping("/list-waiting")
    public String listWaiting() {
        return "exchange/list-waiting";
    }
}
