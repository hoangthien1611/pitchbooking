package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.District;

import java.util.List;

public interface DistrictService {

    List<District> getAllDistricts();

    District getDistrictById(Long id);
}
