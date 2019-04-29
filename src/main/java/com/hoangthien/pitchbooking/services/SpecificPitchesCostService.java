package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.SpecificPitchesCostDTO;

public interface SpecificPitchesCostService {

    SpecificPitchesCostDTO create(SpecificPitchesCostDTO specificPitchesCostDTO);

    void deleteById(Long id);

    SpecificPitchesCostDTO update(SpecificPitchesCostDTO specificPitchesCostDTO);
}
