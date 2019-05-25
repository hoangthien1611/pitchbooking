package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.ExchangeDTO;
import com.hoangthien.pitchbooking.entities.Exchange;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExchangeService {

    boolean save(ExchangeDTO exchangeDTO);

    Page<Exchange> getAllPageable(String path, List<Integer> hasPitch, List<Long> levelIds, String search, int offset);

    Page<Exchange> getAllPageable(String path, List<Integer> hasPitch, List<Long> levelIds, int offset);

    List<Exchange> getAllByUserAndAvailable(String userName);

    boolean delete(Long exchangeId);
}
