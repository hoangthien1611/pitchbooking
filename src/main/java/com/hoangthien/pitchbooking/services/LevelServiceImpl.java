package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.Level;
import com.hoangthien.pitchbooking.repositories.LevelRepository;
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
    public List<Level> getAllLevelsByArea(Long areaId) {
        return levelRepository.findAllByAreaId(areaId);
    }
}
