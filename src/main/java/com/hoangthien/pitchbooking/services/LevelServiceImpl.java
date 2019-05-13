package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.repositories.LevelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {
    @Autowired
    private LevelRepository levelRepository;

    @Override
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @Override
    public List<Level> getAllLevels(String teamSearch) {
        if (StringUtils.isEmpty(teamSearch)) {
            return levelRepository.findAll();
        }
        return levelRepository.findAllByTeamSearch(teamSearch);
    }

    @Override
    public List<Level> getAllLevelsByArea(Long areaId) {
        return levelRepository.findAllByAreaId(areaId);
    }

    @Override
    public List<Level> getAllLevels(Long areaId, String teamSearch) {
        if (StringUtils.isEmpty(teamSearch)) {
            return levelRepository.findAllByAreaId(areaId);
        }

        return levelRepository.findAllByAreaIdAndTeamSearch(areaId, teamSearch);
    }

    @Override
    public List<Level> getAllLevelsByExchange(String path, String search) {
        if (StringUtils.isEmpty(search)) {
            return Defines.DISTRICT_PATH_ALL.equalsIgnoreCase(path)
                    ? levelRepository.findAll()
                    : levelRepository.findAllByDistrictPath(path);
        }
        return Defines.DISTRICT_PATH_ALL.equalsIgnoreCase(path)
                ? levelRepository.findAllByExchangeSearch(search.toLowerCase())
                : levelRepository.findAllByDistrictPathAndExchangeSearch(path, search.toLowerCase());
    }
}
