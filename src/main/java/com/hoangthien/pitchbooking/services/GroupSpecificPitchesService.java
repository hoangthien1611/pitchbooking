package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.GroupSpecificDTO;
import com.hoangthien.pitchbooking.entities.GroupSpecificPitches;

import java.util.List;

public interface GroupSpecificPitchesService {

    GroupSpecificDTO create(GroupSpecificDTO groupSpecificDTO);

    List<GroupSpecificPitches> getAllByPitchId(Long pitchId);

    GroupSpecificDTO delete(Long id);

    GroupSpecificDTO changeNumber(Long id, int number);
}
