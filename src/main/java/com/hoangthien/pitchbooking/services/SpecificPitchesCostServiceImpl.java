package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.constants.Defines;
import com.hoangthien.pitchbooking.dto.SpecificPitchesCostDTO;
import com.hoangthien.pitchbooking.entities.GroupDays;
import com.hoangthien.pitchbooking.entities.GroupSpecificPitches;
import com.hoangthien.pitchbooking.entities.SpecificPitchesCost;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.mapper.SpecificPitchesCostMapper;
import com.hoangthien.pitchbooking.repositories.GroupDaysRepository;
import com.hoangthien.pitchbooking.repositories.GroupSpecificPitchesRepository;
import com.hoangthien.pitchbooking.repositories.SpecificPitchesCostRepository;
import com.hoangthien.pitchbooking.utils.PitchBookingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecificPitchesCostServiceImpl implements SpecificPitchesCostService {

    @Autowired
    private GroupSpecificPitchesRepository groupSpecificPitchesRepository;

    @Autowired
    private SpecificPitchesCostRepository specificPitchesCostRepository;

    @Autowired
    private GroupDaysRepository groupDaysRepository;

    @Autowired
    private SpecificPitchesCostMapper specificPitchesCostMapper;

    @Override
    public SpecificPitchesCostDTO create(SpecificPitchesCostDTO specificPitchesCostDTO) {

        GroupDays groupDays = groupDaysRepository
                .findById(specificPitchesCostDTO.getGroupDaysId())
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy ngày trong tuần!"));

        GroupSpecificPitches groupSpecificPitches = groupSpecificPitchesRepository
                .findById(specificPitchesCostDTO.getGroupSpecificPitchesId())
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy nhóm sân!"));

        SpecificPitchesCost specificPitchesCost = specificPitchesCostMapper
                .specificPitchesCostDTOToSpecificPitchesCost(specificPitchesCostDTO);
        specificPitchesCost.setGroupSpecificPitches(groupSpecificPitches);
        specificPitchesCost.setGroupDays(groupDays);

        return specificPitchesCostMapper
                .specificPitchesCostToSpecificPitchesCostDTO(specificPitchesCostRepository.save(specificPitchesCost));
    }

    @Override
    public void deleteById(Long id) {
        SpecificPitchesCost specificPitchesCost = specificPitchesCostRepository
                .findById(id)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy cost!"));

        specificPitchesCostRepository.deleteById(id);
    }

    @Override
    public SpecificPitchesCostDTO update(SpecificPitchesCostDTO specificPitchesCostDTO) {
        SpecificPitchesCost specificPitchesCost = specificPitchesCostRepository
                .findById(specificPitchesCostDTO.getId())
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy dữ liệu cần update!"));

        GroupDays groupDays = groupDaysRepository
                .findById(specificPitchesCostDTO.getGroupDaysId())
                .orElseThrow(() -> new PitchBookingException("Ngày trong tuần không hợp lệ!"));

        specificPitchesCost.setFromTime(specificPitchesCostDTO.getFromTime());
        specificPitchesCost.setToTime(specificPitchesCostDTO.getToTime());
        specificPitchesCost.setGroupDays(groupDays);
        specificPitchesCost.setCost(specificPitchesCostDTO.getCost());

        return specificPitchesCostMapper
                .specificPitchesCostToSpecificPitchesCostDTO(
                        specificPitchesCostRepository.save(specificPitchesCost)
                );
    }

    @Override
    public List<String> getAllCostsByDistrictPath(String path) {
        List<Integer> list = new ArrayList<>();

        if (Defines.DISTRICT_PATH_ALL.equals(path)) {
            list = specificPitchesCostRepository
                    .findAllDistinctCosts(PageRequest.of(0, 20));

        } else {
            list = specificPitchesCostRepository
                    .findAllDistinctCostsByDistrictPath(path);
        }

        return PitchBookingUtils.getListCostCommafyFromListCostInt(list);
    }
}
