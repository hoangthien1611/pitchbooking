package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.DistrictDTO;
import com.hoangthien.pitchbooking.entities.District;
import org.mapstruct.Mapper;

@Mapper
public interface DistrictMapper {

    DistrictDTO districtToDistrictDTO(District district);
}
