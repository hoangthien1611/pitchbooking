package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.entities.GroupSpecificPitches;
import com.hoangthien.pitchbooking.entities.Pitch;
import com.hoangthien.pitchbooking.entities.PitchType;
import com.hoangthien.pitchbooking.entities.YardSurface;
import com.hoangthien.pitchbooking.exception.PitchBookingNotFoundException;
import com.hoangthien.pitchbooking.exception.PitchBookingUnauthorizedException;
import com.hoangthien.pitchbooking.services.*;
import com.hoangthien.pitchbooking.utils.PitchBookingUtils;
import com.hoangthien.pitchbooking.utils.TimeUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(PitchController.BASE_URL)
@Log4j2
public class PitchController extends BaseController {

    public static final String BASE_URL = "/pitch";

    @Autowired
    private YardSurfaceService yardSurfaceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PitchService pitchService;

    @Autowired
    private GroupDaysService groupDaysService;

    @Autowired
    private PitchTypeService pitchTypeService;

    @Autowired
    private GroupSpecificPitchesService groupSpecificPitchesService;

    @Autowired
    private SpecificPitchesCostService specificPitchesCostService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/management/create")
    public String create(Model model) {
        log.info("GET: " + BASE_URL + "/create");
        model.addAttribute("listDistricts", districtService.getAllDistricts());
        model.addAttribute("listSurfaces", yardSurfaceService.getAllYardSurfaces());
        return "pitch/create";
    }

    @PostMapping("/management/create")
    public String create(@Valid @ModelAttribute PitchDTO pitchDTO, BindingResult rs,
                         @RequestParam("imgAvatar") MultipartFile avatar, RedirectAttributes ra,
                         Principal principal) {
        log.info("POST: " + BASE_URL + "/create");

        if (rs.hasErrors()) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng nhập đầy đủ thông tin phù hợp!"));
            return "redirect:/pitch/management/create";
        }

        try {
            if (!avatar.getOriginalFilename().isEmpty()) {
                pitchDTO.setAvatar(fileService.saveFile(avatar));
            } else {
                pitchDTO.setAvatar("default-pitch.jpg");
            }

            pitchDTO.setOwnerUserName(principal.getName());

            pitchService.saveNewPitch(pitchDTO);
            ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Thêm sân bóng mới thành công"));
            return "redirect:/pitch/management";

        } catch (Exception e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
            return "redirect:/pitch/management/create";
        }
    }

    @GetMapping("/management")
    public String ownPitches(Model model, Principal principal) {
        log.info("GET: " + BASE_URL + "/management");
        List<Pitch> pitches = pitchService.getPitchesByOwner(principal.getName());
        model.addAttribute("pitches", pitches);
        if (pitches.size() > 0) {
            model.addAttribute("totalRequests", bookingService.getAllByUserNameAndNotAccepted(principal.getName()).size());
        }
        return "pitch/own-pitches";
    }

    @GetMapping("/management/booking-requests")
    public String getBookingRequest(Model model, Principal principal) {
        log.info("GET: " + BASE_URL + "/management/booking-requests");
        model.addAttribute("bookings", bookingService.getAllByUserNameAndNotAccepted(principal.getName()));
        return "pitch/booking-requests";
    }

    @GetMapping("/management/pitch-info/{pitchId}")
    public String info(Model model, @PathVariable("pitchId") String pitch, Principal principal) {
        log.info("GET: " + BASE_URL + "/management/pitch-info/{pitchId}");
        try {
            Long pitchId = Long.valueOf(Integer.parseInt(pitch));
            Pitch pitchFound = pitchService.getPitchById(pitchId);
            if (!pitchFound.getOwner().getUserName().equals(principal.getName())) {
                throw new PitchBookingUnauthorizedException("Unauthorized!!!");
            }
            model.addAttribute("pitch", pitchFound);
            model.addAttribute("listDistricts", districtService.getAllDistricts());
            model.addAttribute("listSurfaces", yardSurfaceService.getAllYardSurfaces());
            return "pitch/edit";
        } catch (PitchBookingNotFoundException | NumberFormatException e) {
            log.error(e.getMessage());
            return "error/page_404";
        } catch (PitchBookingUnauthorizedException e) {
            log.error(e.getMessage());
            return "error/page_403";
        }
    }

    @PostMapping("/management/pitch-info/edit")
    public String edit(@Valid @ModelAttribute PitchDTO pitchDTO, BindingResult rs,
                       @RequestParam("imgAvatar") MultipartFile avatar, RedirectAttributes ra) {
        log.info("POST: " + BASE_URL + "/management/pitch-info/edit");

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

    @GetMapping("/management/pitch-prices/{pitchId}")
    public String getPrices(Model model, @PathVariable("pitchId") String pitch, Principal principal) {
        log.info("GET: " + BASE_URL + "/management/pitch-prices/{pitchId}");
        try {
            Long pitchId = Long.valueOf(Integer.parseInt(pitch));

            List<GroupSpecificPitches> groupSpecificPitches = groupSpecificPitchesService.getAllByPitchId(pitchId);
            List<PitchType> pitchTypes = pitchTypeService.getAll();
            List<PitchType> pitchTypesAfterFilter = pitchTypes.stream()
                    .filter(pitchType -> !isPitchTypeExistedInGroupSpecificPitches(pitchType.getId(), groupSpecificPitches))
                    .collect(Collectors.toList());

            Pitch pitchFound = pitchService.getPitchById(pitchId);
            if (!pitchFound.getOwner().getUserName().equals(principal.getName())) {
                throw new PitchBookingUnauthorizedException("Unauthorized!!!");
            }

            model.addAttribute("specificPitches", groupSpecificPitches);
            model.addAttribute("pitchId", pitchId);
            model.addAttribute("pitchName", pitchFound.getName());
            model.addAttribute("groupDaysList", groupDaysService.getAll());
            model.addAttribute("pitchTypeList", pitchTypesAfterFilter);
            model.addAttribute("listTimeFrame", TimeUtils.getTimeStringsFromStartToEnd(Defines.TIME_START, Defines.TIME_END));
            return "pitch/prices";
        } catch (PitchBookingNotFoundException | NumberFormatException e) {
            log.error(e.getMessage());
            return "error/page_404";
        } catch (PitchBookingUnauthorizedException e) {
            log.error(e.getMessage());
            return "error/page_403";
        }
    }

    @GetMapping("/management/pitch-bookings/{pitchId}")
    public String booking(Model model, @PathVariable("pitchId") String pitch,
                          @RequestParam(value = "date", required = false) String date, Principal principal) {
        log.info("GET: " + BASE_URL + "/management/pitch-bookings/{pitchId}");
        try {
            Long pitchId = Long.valueOf(Integer.parseInt(pitch));
            LocalDate dateBooking = TimeUtils.getLocalDateFromDateString(date);

            Pitch pitchFound = pitchService.getPitchById(pitchId);
            if (!pitchFound.getOwner().getUserName().equals(principal.getName())) {
                throw new PitchBookingUnauthorizedException("Unauthorized!!!");
            }

            model.addAttribute("date", dateBooking.toString());
            model.addAttribute("pitchId", pitchId);
            model.addAttribute("pitchName", pitchFound.getName());
            model.addAttribute("timeFrameBookings", pitchService.getTimeFrameBookingsByDate(pitchId, dateBooking));
            return "pitch/bookings";
        } catch (PitchBookingNotFoundException | NumberFormatException e) {
            log.error(e.getMessage());
            return "error/page_404";
        } catch (PitchBookingUnauthorizedException e) {
            log.error(e.getMessage());
            return "error/page_403";
        }
    }

    @DeleteMapping("/management/{pitchId}")
    @ResponseBody
    public boolean deletePitch(@PathVariable("pitchId") Long pitchId) {
        log.info("DELETE: pitch/management/" + pitchId);
        try {
            return pitchService.deletePitch(pitchId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @GetMapping("/{path}")
    public String search(Model model, @PathVariable("path") String path, @RequestParam(value = "pg", defaultValue = "1") String pg,
                         @RequestParam(value = "c", defaultValue = "") String cValue,
                         @RequestParam(value = "t", defaultValue = "") String tValue,
                         @RequestParam(value = "f", defaultValue = "") String fValue,
                         @RequestParam(value = "s", defaultValue = "") String search) {
        log.info("GET: " + BASE_URL + "/" + path);
        try {
            int page = Integer.parseInt(pg);
            page = page < 1 ? 0 : (page - 1);
            List<Integer> intCosts = specificPitchesCostService.getAllCostsByDistrictPath(path);
            List<PitchType> pitchTypes = pitchTypeService.getAll();
            List<YardSurface> yardSurfaces = yardSurfaceService.getAllYardSurfaces();

            List<Integer> costValues = StringUtils.isEmpty(cValue) ? intCosts : PitchBookingUtils.convertFromStringListToIntList(cValue);
            List<Long> typeIdValues = StringUtils.isEmpty(tValue) ? getTypeIdsFromTypes(pitchTypes) : PitchBookingUtils.convertFromStringListToLongList(tValue);
            List<Long> surfaceIdValues = StringUtils.isEmpty(fValue) ? getSurfaceIdsFromSurfaces(yardSurfaces) : PitchBookingUtils.convertFromStringListToLongList(fValue);
            Page<Pitch> pagePitches;

            if (Defines.DISTRICT_PATH_ALL.equalsIgnoreCase(path)) {
                if (StringUtils.isEmpty(search)) {
                    pagePitches = pitchService.getAllPageable(costValues, typeIdValues, surfaceIdValues, page);
                } else {
                    pagePitches = pitchService.getAllPageable(costValues, typeIdValues, surfaceIdValues, search, page);
                }
            } else {
                if (StringUtils.isEmpty(search)) {
                    pagePitches = costValues.size() > 0 ? pitchService.getAllPageable(path, costValues, typeIdValues, surfaceIdValues, page)
                            : pitchService.getAllPageable(path, typeIdValues, surfaceIdValues, page);
                } else {
                    pagePitches = costValues.size() > 0 ? pitchService.getAllPageable(path, costValues, typeIdValues, surfaceIdValues, search, page)
                            : pitchService.getAllPageable(path, typeIdValues, surfaceIdValues, search, page);
                }
                model.addAttribute("districtName", districtService.getDistrictDTOByPath(path).getName());
            }

            int totalPages = pagePitches.getTotalPages();
            if (totalPages > 0) {
                int pageEnd = (totalPages < 5) ? totalPages : 5;
                int pageStart = 1;
                if (page > 3) {
                    pageEnd = ((page + 2) <= totalPages) ? (page + 2) : totalPages;
                    pageStart = ((pageEnd - 4) < 1) ? 1 : (pageEnd - 4);
                }
                model.addAttribute("pageStart", pageStart);
                model.addAttribute("pageEnd", pageEnd);
            }

            model.addAttribute("pitches", pagePitches.getContent());
            model.addAttribute("currentPage", (page + 1));
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("path", path);
            model.addAttribute("pitchTypes", pitchTypes);
            model.addAttribute("yardSurfaces", yardSurfaces);
            model.addAttribute("totalPitches", pagePitches.getTotalElements());
            model.addAttribute("costs", PitchBookingUtils.getCostListFromIntList(intCosts));
            model.addAttribute("cValue", cValue);
            model.addAttribute("tValue", tValue);
            model.addAttribute("fValue", fValue);
            model.addAttribute("search", search);
            return "pitch/pitches";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }

    @GetMapping("/detail/{pitchId}")
    public String detail(Model model, @PathVariable("pitchId") String pitch) {
        log.info("GET: " + BASE_URL + "/detail/" + pitch);
        try {
            Long pitchId = Long.valueOf(Integer.parseInt(pitch));
            Pitch pitchDetail = pitchService.getPitchById(pitchId);
            model.addAttribute("pitch", pitchDetail);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("pitchesSameDistrict", pitchService.get3PitchesSameDistrict(pitchId, pitchDetail.getDistrict().getId()));
            return "pitch/detail";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }

    @GetMapping("/get-all/{districtId}")
    @ResponseBody
    public List<PitchDTO> getPitch(@PathVariable("districtId") Long districtId) {
        log.info("GET: " + BASE_URL + "/get-all/" + districtId);
        return pitchService.getAllByDistrict(districtId);
    }

    private boolean isPitchTypeExistedInGroupSpecificPitches(Long pitchtypeId, List<GroupSpecificPitches> groupSpecificPitches) {
        return groupSpecificPitches.stream()
                .anyMatch(gr -> gr.getPitchType().getId() == pitchtypeId);
    }

    private List<Long> getTypeIdsFromTypes(List<PitchType> list) {
        return list.stream()
                .map(pitchType -> pitchType.getId())
                .collect(Collectors.toList());
    }

    private List<Long> getSurfaceIdsFromSurfaces(List<YardSurface> list) {
        return list.stream()
                .map(yardSurface -> yardSurface.getId())
                .collect(Collectors.toList());
    }
}
