package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.DistrictDTO;
import com.hoangthien.pitchbooking.entities.District;

import java.util.List;

public interface DistrictService {

    List<District> getAllDistricts();

    List<District> getAllDistricts(List<Long> levelIds, String searchTeam);

    List<DistrictDTO> getAllDistrictDTOS();

    District getDistrictById(Long id);

    DistrictDTO getDistrictDTOByPath(String path);
}
