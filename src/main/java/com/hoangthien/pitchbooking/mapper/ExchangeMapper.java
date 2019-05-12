package com.hoangthien.pitchbooking.mapper;

import com.hoangthien.pitchbooking.dto.ExchangeDTO;
import com.hoangthien.pitchbooking.entities.Exchange;
import org.mapstruct.Mapper;

@Mapper
public interface ExchangeMapper {

    Exchange exchangeDTOToExchange(ExchangeDTO exchangeDTO);
}
