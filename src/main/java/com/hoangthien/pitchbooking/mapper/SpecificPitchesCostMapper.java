package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.SpecificPitchesCostDTO;
import com.hoangthien.pitchbooking.entities.SpecificPitchesCost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpecificPitchesCostMapper {

    SpecificPitchesCostMapper INSTANCE = Mappers.getMapper(SpecificPitchesCostMapper.class);

    SpecificPitchesCost specificPitchesCostDTOToSpecificPitchesCost(SpecificPitchesCostDTO specificPitchesCostDTO);

    @Mapping(source = "groupDays.name", target = "groupDaysName")
    SpecificPitchesCostDTO specificPitchesCostToSpecificPitchesCostDTO(SpecificPitchesCost specificPitchesCost);
}
