package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.Level;

import java.util.List;

public interface LevelService {

    List<Level> getAllLevels();

    List<Level> getAllLevels(String teamSearch);

    List<Level> getAllLevelsByArea(Long areaId);

    List<Level> getAllLevels(Long areaId, String teamSearch);

    List<Level> getAllLevelsByExchange(String path, String search);
}
