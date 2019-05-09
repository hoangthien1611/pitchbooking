package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.DistrictDTO;
import com.hoangthien.pitchbooking.entities.District;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.DistrictMapper;
import com.hoangthien.pitchbooking.repositories.DistrictRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Override
    public List<District> getAllDistricts(List<Long> levelIds, String searchTeam) {
        if (StringUtils.isEmpty(searchTeam)) {
            return districtRepository.findAllByLevelIdIn(levelIds);
        }

        return districtRepository.findAllByLevelIdInAndSearchTeam(levelIds, searchTeam);
    }

    @Transactional
    @Override
    public List<DistrictDTO> getAllDistrictDTOS() {
        List<District> districts = districtRepository.findAll();
        return districts.stream()
                .map(district -> {
                    DistrictDTO districtDTO = districtMapper.districtToDistrictDTO(district);
                    districtDTO.setTotalPitches(district.getPitches().size());
                    return districtDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public District getDistrictById(Long id) {
        return districtRepository
                .findById(id)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy district!"));
    }

    @Override
    public DistrictDTO getDistrictDTOByPath(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new PitchBookingException("Path is not valid");
        }

        District district = districtRepository
                .findFirstByPath(path)
                .orElseThrow(() -> new PitchBookingException("District not found with this path: " + path));

        return districtMapper.districtToDistrictDTO(district);
    }
}
