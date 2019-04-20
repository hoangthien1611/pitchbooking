package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.YardSurface;
import com.hoangthien.pitchbooking.repositories.YardSurfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YardSurfaceServiceImpl implements YardSurfaceService {

    @Autowired
    private YardSurfaceRepository yardSurfaceRepository;

    @Override
    public List<YardSurface> getAllYardSurfaces() {
        return yardSurfaceRepository.findAll();
    }
}
