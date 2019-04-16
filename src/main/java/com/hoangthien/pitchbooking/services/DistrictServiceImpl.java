package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.District;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Override
    public District getDistrictById(Long id) {
        return districtRepository
                .findById(id)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy district!"));
    }
}
