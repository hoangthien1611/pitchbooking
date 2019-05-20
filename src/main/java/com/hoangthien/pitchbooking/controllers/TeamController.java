package com.hoangthien.pitchbooking.controllers;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.constants.MessageType;
import com.hoangthien.pitchbooking.dto.Message;
import com.hoangthien.pitchbooking.dto.TeamDTO;
import com.hoangthien.pitchbooking.entities.District;
import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.entities.Team;
import com.hoangthien.pitchbooking.entities.User;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.exception.PitchBookingUnauthorizedException;
import com.hoangthien.pitchbooking.services.DistrictService;
import com.hoangthien.pitchbooking.services.FileService;
import com.hoangthien.pitchbooking.services.LevelService;
import com.hoangthien.pitchbooking.services.TeamService;
import com.hoangthien.pitchbooking.utils.PitchBookingUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(TeamController.BASE_URL)
@Log4j2
public class TeamController extends BaseController {

    public static final String BASE_URL = "/team";

    @Autowired
    private DistrictService districtService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TeamService teamService;

    @GetMapping("/create")
    public String create(Model model) {
        log.info("GET: " + BASE_URL + "/create");
        model.addAttribute("listDistricts", districtService.getAllDistricts());
        model.addAttribute("listLevels", levelService.getAllLevels());
        return "team/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TeamDTO teamDTO, BindingResult rs, @RequestParam("imgLogo") MultipartFile logo,
                         @RequestParam("imgTeam") MultipartFile banner, RedirectAttributes ra, Principal principal) {
        log.info("POST: " + BASE_URL + "/create");
        if (rs.hasErrors()) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, "Vui lòng nhập đầy đủ thông tin phù hợp!"));
            return "redirect:/team/create";
        }

        try {
            if (!logo.getOriginalFilename().isEmpty()) {
                teamDTO.setLogo(fileService.saveFile(logo));
            }

            if (!banner.getOriginalFilename().isEmpty()) {
                teamDTO.setPicture(fileService.saveFile(banner));
            }

            teamService.saveNewTeam(teamDTO, principal.getName());
        } catch (Exception e) {
            ra.addFlashAttribute("msg", new Message(MessageType.ERROR, e.getMessage()));
            return "redirect:/team/create";
        }

        ra.addFlashAttribute("msg", new Message(MessageType.SUCCESS, "Thêm đội bóng mới thành công"));
        return "redirect:/team/detail/" + teamDTO.getPath();
    }

    @GetMapping("/check-path")
    @ResponseBody
    public boolean checkPath(@RequestParam("path") String path) {
        log.info("GET: " + BASE_URL + "/check-path");
        return teamService.isPathExisted(path);
    }

    @GetMapping("/my-teams")
    public String getMyTeams(Model model, Principal principal) {
        log.info("GET: /team/my-teams");
        model.addAttribute("teams", teamService.getAllTeamsUserIn(principal.getName()));
        return "team/my-teams";
    }

    @DeleteMapping("/{teamId}")
    @ResponseBody
    public boolean delete(@PathVariable("teamId") Long teamId, Principal principal) {
        log.info("DELETE: /team/" + teamId);
        try {
            Team teamFound = teamService.getTeamById(teamId);
            if (!teamFound.getCaptain().getUserName().equals(principal.getName())) {
                throw new PitchBookingUnauthorizedException("Unauthorized!!!");
            }

            return teamService.delete(teamId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @GetMapping("/edit/{teamId}")
    public String edit(Model model, @PathVariable("teamId") String team) {
        log.info("GET: /team/edit/" + team);
        try {
            Long teamId = Long.valueOf(Integer.parseInt(team));
            model.addAttribute("team", teamService.getTeamById(teamId));
            model.addAttribute("listLevels", levelService.getAllLevels());
            return "team/edit";
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }

    @GetMapping("/detail/{path}")
    public String detail(Model model, @PathVariable("path") String path, @RequestParam(value = "tab", defaultValue = "1") String tabStr) {
        log.info("GET: " + BASE_URL + "/detail/" + path);
        try {
            int tab = Integer.parseInt(tabStr);
            if (tab < 1 || tab > 4) {
                throw new PitchBookingException("Tab không hợp lệ!");
            }
            Team team = teamService.getTeamByPath(path.trim());

            List<User> members = new ArrayList<>();
            members.add(team.getCaptain());
            if (!CollectionUtils.isEmpty(team.getMembers())) {
                members.addAll(team.getMembers());
            }

            model.addAttribute("team", team);
            model.addAttribute("tab", tab);
            model.addAttribute("members", members);
            model.addAttribute("teamsSameLevel", teamService.get5TeamsSameLevel(team.getId(), team.getLevel().getId()));
            return "team/detail";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "error/page_404";
    }

    @GetMapping("/search")
    public String getTeams(Model model, @RequestParam(value = "a", defaultValue = "0") String area,
                           @RequestParam(value = "l", defaultValue = "") String level,
                           @RequestParam(value = "p", defaultValue = "1") String pg,
                           @RequestParam(value = "s", defaultValue = "") String search) {
        log.info("GET: " + BASE_URL + "/search");

        try {
            Page<Team> pages;
            Long areaId = Long.valueOf(Integer.parseInt(area));
            List<Level> levelList = new ArrayList<>();
            List<Long> levelIds = new ArrayList<>();
            int page = Integer.parseInt(pg);
            int offset = (page - 1) * Defines.NUMBER_OF_ROWS_PER_PAGE;

            if (areaId == 0) {
                levelList = levelService.getAllLevels(search);
                levelIds = StringUtils.isEmpty(level) ? PitchBookingUtils.getIdsFromLevels(levelList)
                        : PitchBookingUtils.convertFromStringListToLongList(level);

                if (StringUtils.isEmpty(search)) {
                    pages = teamService.getAllTeamsPageable(levelIds, offset);
                } else {
                    pages = teamService.getAllTeamsPageable(levelIds, search, offset);
                }

                model.addAttribute("listLevels", levelList);
            } else {
                levelList = levelService.getAllLevels(areaId, search);
                levelIds = StringUtils.isEmpty(level) ? PitchBookingUtils.getIdsFromLevels(levelList)
                        : PitchBookingUtils.convertFromStringListToLongList(level);

                if (StringUtils.isEmpty(search)) {
                    pages = teamService.getAllTeamsPageable(areaId, levelIds, offset);
                } else {
                    pages = teamService.getAllTeamsPageable(areaId, levelIds, search, offset);
                }

                model.addAttribute("areaName", districtService.getDistrictById(areaId).getName());
                model.addAttribute("listLevels", levelList);
            }

            int totalPages = pages.getTotalPages();
            if (totalPages > 0) {
                int pageEnd = (totalPages < 5) ? totalPages : 5;
                int pageStart = 1;
                if (page > 3) {
                    pageEnd = ((page + 2) < totalPages) ? (page + 2) : totalPages;
                    pageStart = ((pageEnd - 4) < 1) ? 1 : (pageEnd - 4);
                }
                model.addAttribute("pageStart", pageStart);
                model.addAttribute("pageEnd", pageEnd);
            }

            District district = new District(0L, "Tất cả", "all", new ArrayList<>(), new ArrayList<>());
            List<District> listDistricts = districtService.getAllDistricts();
            listDistricts.add(0, district);

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("aValue", area);
            model.addAttribute("lValue", level);
            model.addAttribute("search", search);
            model.addAttribute("listDistricts", listDistricts);
            model.addAttribute("totalTeams", teamService.countTotalTeams());
            model.addAttribute("teams", pages.getContent());
            model.addAttribute("totalTeamsFound", pages.getTotalElements());
            return "team/list";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error/page_404";
        }
    }
}
