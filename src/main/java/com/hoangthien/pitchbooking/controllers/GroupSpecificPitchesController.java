package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.dto.GroupSpecificDTO;
import com.hoangthien.pitchbooking.services.GroupSpecificPitchesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(GroupSpecificPitchesController.BASE_URL)
@Log4j2
public class GroupSpecificPitchesController {

    public static final String BASE_URL = "/group-specific-pitches";

    @Autowired
    private GroupSpecificPitchesService groupSpecificPitchesService;

    @PostMapping
    @ResponseBody
    public GroupSpecificDTO create(@ModelAttribute GroupSpecificDTO groupSpecificDTO) {
        log.info("POST: " + BASE_URL);
        try {
            return groupSpecificPitchesService.create(groupSpecificDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        log.info("DELETE: " + BASE_URL);
        try {
            groupSpecificPitchesService.delete(id);
            return "SUCCESS";
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
