package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.UserDTO;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.services.BookingService;
import com.hoangthien.pitchbooking.services.InvitationService;
import com.hoangthien.pitchbooking.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@Log4j2
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/check-userName")
    @ResponseBody
    public String checkUserNameExisted(@RequestParam("userName") String userName) {
        log.info("POST: /user/check-userName");
        if (userService.isUserNameExisted(userName)) {
            return "Existed";
        }
        return null;
    }

    @GetMapping("/profile")
    public String getUserInfo(Model model, Principal principal, RedirectAttributes ra) {
        log.info("GET: /user/profile");
        if (principal == null) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng đăng nhập trước!"));
            return "redirect:/";
        }

        model.addAttribute("user", userService.getUser(principal.getName()));
        return "profile/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Model model, @ModelAttribute UserDTO userDTO, RedirectAttributes ra) {
        log.info("POST: /user/profile/update");
        try {
            model.addAttribute("user", userService.updateProfile(userDTO));
            ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Cập nhật thông tin thành công!"));
            return "redirect:/user/profile";
        } catch (PitchBookingException e) {
            model.addAttribute("user", userService.getUser(userDTO.getId()));
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Cập nhật thông tin thất bại!"));
            return "redirect:/user/profile";
        } catch (PitchBookingNotFoundException e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam("id") Long id, @RequestParam("currentPassword") String currentPass,
                                 @RequestParam("newPassword") String newPass, RedirectAttributes ra) {
        log.info("POST: /user/profile/change-password");
        try {
            if (userService.changePassword(id, currentPass, newPass)) {
                ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Đổi mật khẩu thành công"));
                return "redirect:/user/profile";
            }

            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Đổi mật khẩu thất bại! Mật khẩu hiện tại không đúng!"));
            return "redirect:/user/profile";
        } catch (PitchBookingNotFoundException e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }

    @GetMapping("/booking-history")
    public String getBookingHistory(Model model, Principal principal) {
        log.info("GET: /user/booking-history");
        model.addAttribute("bookings", bookingService.getAllBookingsOfAUser(principal.getName()));
        return "profile/booking-history";
    }

    @GetMapping("/invitation-history")
    public String invitationHistory(Model model, Principal principal) {
        log.info("GET: /user/invitation-history");
        model.addAttribute("invitations", invitationService.getAllOfAUser(principal.getName()));
        return "profile/invitation-history";
    }
}
