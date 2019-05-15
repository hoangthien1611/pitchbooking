package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.dto.BookingCheck;
import com.hoangthien.pitchbooking.dto.BookingDTO;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.services.BookingService;
import com.hoangthien.pitchbooking.utils.TimeUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(BookingController.BASE_URL)
@Log4j2
public class BookingController {

    public static final String BASE_URL = "/booking";

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @ResponseBody
    public BookingDTO save(@ModelAttribute BookingDTO bookingDTO, Principal principal) {
        log.info("POST: " + BASE_URL);
        try {
            bookingDTO.setUserName(principal.getName());
            return bookingService.save(bookingDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/{bookingId}")
    @ResponseBody
    public String delete(@PathVariable("bookingId") Long bookingId) {
        log.info("DELETE: " + BASE_URL + "/" + bookingId);
        try {
            bookingService.delete(bookingId);
            return "SUCCESS";
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/get-booking-check-list")
    @ResponseBody
    public List<BookingCheck> getListAvailable(@RequestParam("pitchesCostId") Long pitchesCostId, @RequestParam("date") String date) {
        log.info("GET: " + BASE_URL + "/get-booking-check-list");
        try {
            return bookingService.getBookingCheckList(pitchesCostId, TimeUtils.getLocalDateFromDateString(date));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @PostMapping("/for-user")
    @ResponseBody
    public String savePublic(@ModelAttribute BookingDTO bookingDTO, Principal principal) {
        log.info("POST: " + BASE_URL + "/for-user");
        try {
            bookingDTO.setUserName(principal.getName());
            if (bookingService.saveForUser(bookingDTO) != null) {
                return "SUCCESS";
            }
            return "Không còn sân trống cho khoảng thời gian bạn chọn!";
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @PatchMapping("/{bookingId}")
    @ResponseBody
    public boolean accept(@PathVariable("bookingId") Long bookingId) {
        log.info("PATCH: " + BASE_URL + "/" + bookingId);
        try {
            return bookingService.acceptBooking(bookingId);
        } catch (PitchBookingNotFoundException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
