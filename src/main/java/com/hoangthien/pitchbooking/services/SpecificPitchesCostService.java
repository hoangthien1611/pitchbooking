package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.Cost;
import com.hoangthien.pitchbooking.dto.SpecificPitchesCostDTO;

import java.util.List;

public interface SpecificPitchesCostService {

    SpecificPitchesCostDTO create(SpecificPitchesCostDTO specificPitchesCostDTO);

    void deleteById(Long id);

    SpecificPitchesCostDTO update(SpecificPitchesCostDTO specificPitchesCostDTO);

    List<Cost> getAllCostsByDistrictPath(String path);
}
