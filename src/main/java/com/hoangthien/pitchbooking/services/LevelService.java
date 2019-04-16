package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.Level;

import java.util.List;

public interface LevelService {

    List<Level> getAllLevels();

    List<Level> getAllLevelsByArea(Long areaId);
}
