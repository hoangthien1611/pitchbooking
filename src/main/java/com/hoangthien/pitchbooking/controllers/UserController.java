package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/check-userName")
    @ResponseBody
    public String checkUserNameExisted(@RequestParam("userName") String userName) {
        log.info("POST: /user/check-userName");
        if (userService.isUserNameExisted(userName)) {
            return "Existed";
        }
        return null;
    }
}
