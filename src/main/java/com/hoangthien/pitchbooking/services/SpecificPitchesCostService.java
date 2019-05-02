package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.SpecificPitchesCostDTO;

import java.util.List;

public interface SpecificPitchesCostService {

    SpecificPitchesCostDTO create(SpecificPitchesCostDTO specificPitchesCostDTO);

    void deleteById(Long id);

    SpecificPitchesCostDTO update(SpecificPitchesCostDTO specificPitchesCostDTO);

    List<String> getAllCostsByDistrictPath(String path);
}
