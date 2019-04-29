package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.dto.SpecificPitchesCostDTO;
import com.hoangthien.pitchbooking.services.SpecificPitchesCostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(SpecificPitchesCostController.BASE_URL)
@Log4j2
public class SpecificPitchesCostController {

    public static final String BASE_URL = "/specific-pitches-cost";

    @Autowired
    private SpecificPitchesCostService specificPitchesCostService;

    @PostMapping
    @ResponseBody
    public SpecificPitchesCostDTO create(@ModelAttribute SpecificPitchesCostDTO specificPitchesCostDTO) {
        log.info("POST: " + BASE_URL);
        try {
            return specificPitchesCostService.create(specificPitchesCostDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        log.info("DELETE: " + BASE_URL + "/" + id);
        try {
            specificPitchesCostService.deleteById(id);
            return "SUCCESS";
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public SpecificPitchesCostDTO update(@PathVariable("id") Long id, @ModelAttribute SpecificPitchesCostDTO specificPitchesCostDTO) {
        log.info("PUT: " + BASE_URL + "/" + id);
        try {
            specificPitchesCostDTO.setId(id);
            return specificPitchesCostService.update(specificPitchesCostDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
