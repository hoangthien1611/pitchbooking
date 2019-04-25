package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.GroupSpecificDTO;
import com.hoangthien.pitchbooking.entities.GroupSpecificPitches;

import java.util.List;

public interface GroupSpecificPitchesService {

    GroupSpecificDTO create(GroupSpecificDTO groupSpecificDTO);

    List<GroupSpecificPitches> getAllByPitchId(Long pitchId);

    void delete(Long id);
}
