package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.services.DistrictService;
import com.hoangthien.pitchbooking.services.FileService;
import com.hoangthien.pitchbooking.services.PitchService;
import com.hoangthien.pitchbooking.services.YardSurfaceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(PitchController.BASE_URL)
@Log4j2
public class PitchController {

    public static final String BASE_URL = "/pitch";

    @Autowired
    private YardSurfaceService yardSurfaceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PitchService pitchService;

    @GetMapping("/management/create")
    public String create(Model model) {
        log.info("GET: " + BASE_URL + "/create");
        model.addAttribute("listDistricts", districtService.getAllDistricts());
        model.addAttribute("listSurfaces", yardSurfaceService.getAllYardSurfaces());
        return "pitch/create";
    }

    @PostMapping("/management/create")
    public String create(@Valid @ModelAttribute PitchDTO pitchDTO, BindingResult rs,
                         @RequestParam("imgAvatar") MultipartFile avatar, RedirectAttributes ra) {
        log.info("POST: " + BASE_URL + "/create");

        if (rs.hasErrors()) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng nhập đầy đủ thông tin phù hợp!"));
            return "redirect:/pitch/management/create";
        }

        try {
            if (!avatar.getOriginalFilename().isEmpty()) {
                pitchDTO.setAvatar(fileService.saveFile(avatar));
            }

            pitchService.saveNewPitch(pitchDTO);
            ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Thêm sân bóng mới thành công"));
            return "redirect:/pitch/management";

        } catch (Exception e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
            return "redirect:/pitch/management/create";
        }
    }

    @GetMapping("/management")
    public String ownPitches(Model model) {
        log.info("GET: " + BASE_URL + "/management");
        model.addAttribute("pitches", pitchService.getPitchesByOwnerId(1L));
        return "pitch/own-pitches";
    }

    @GetMapping("/management/pitch-info/{pitchId}")
    public String info(Model model, @PathVariable("pitchId") String pitch) {
        log.info("GET: " + BASE_URL + "/management/pitch-info/{pitchId}");
        try {
            Long pitchId = Long.valueOf(Integer.parseInt(pitch));
            model.addAttribute("pitch", pitchService.getPitchById(pitchId));
            model.addAttribute("listDistricts", districtService.getAllDistricts());
            model.addAttribute("listSurfaces", yardSurfaceService.getAllYardSurfaces());
            return "pitch/edit";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }

    @PostMapping("/management/pitch-info/edit")
    public String edit(@Valid @ModelAttribute PitchDTO pitchDTO, BindingResult rs,
                       @RequestParam("imgAvatar") MultipartFile avatar, RedirectAttributes ra) {
        log.info("POST: " + BASE_URL + "/management/pitch-info/{pitchId}");

        if (rs.hasErrors()) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng nhập đầy đủ thông tin phù hợp!"));
        }

        try {
            if (!avatar.getOriginalFilename().isEmpty()) {
                pitchDTO.setAvatar(fileService.saveFile(avatar));
            }

            pitchService.updatePitch(pitchDTO);
            ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Cập nhật sân bóng thành công"));

        } catch (Exception e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
        }
        return "redirect:/pitch/management/pitch-info/" + pitchDTO.getId();
    }
}
