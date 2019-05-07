package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.UserDTO;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
@Log4j2
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(HttpServletRequest request, @RequestParam(required = false) String message, Model model) {
        log.info("GET: /");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principal)) {
            return "redirect:/pitch/all";
        }

        if (!StringUtils.isEmpty(message)) {
            if (message.equals("logout")) {
                model.addAttribute("msg", new Message(MessageType.SUCCESS, "Bạn đã logout thành công!"));
            }
            if (message.equals("error")) {
                model.addAttribute("msg", new Message(MessageType.ERROR, "Sai username hoặc mật khẩu!"));
            }
            if (message.equals("timeout")) {
                model.addAttribute("msg", new Message(MessageType.ERROR, "Time out! Vui lòng đăng nhập lại!"));
            }
            if (message.equals("max_session")) {
                model.addAttribute("msg", new Message(MessageType.ERROR, "Tài khoản này đã được đăng nhập ở một nơi khác!"));
            }
        }

        return "home/index";
    }

    @GetMapping("/register")
    public String register() {
        log.info("GET: /register");
        return "home/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO, RedirectAttributes ra) {
        log.info("POST: /register");
        try {
            userService.save(userDTO);
            ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Bạn đã đăng ký thành công! Bây giờ có thể đăng nhập tại đây"));
            return "redirect:/";
        } catch (PitchBookingException e) {
            log.error(e.getMessage());
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
            return "redirect:/register";
        }
    }
}
