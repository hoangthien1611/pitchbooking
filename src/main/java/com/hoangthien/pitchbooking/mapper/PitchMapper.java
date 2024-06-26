package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.PitchDTO;
import com.hoangthien.pitchbooking.entities.Pitch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PitchMapper {

    PitchMapper INSTANCE = Mappers.getMapper(PitchMapper.class);

    Pitch pitchDTOToPitch(PitchDTO pitchDTO);

    @Mapping(source = "district.id", target = "districtId")
    PitchDTO pitchToPitchDTO(Pitch pitch);
}
