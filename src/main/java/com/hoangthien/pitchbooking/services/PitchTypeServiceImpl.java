package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.PitchType;
import com.hoangthien.pitchbooking.repositories.PitchTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PitchTypeServiceImpl implements PitchTypeService {

    @Autowired
    private PitchTypeRepository pitchTypeRepository;

    @Override
    public List<PitchType> getAll() {
        return pitchTypeRepository.findAll();
    }
}
