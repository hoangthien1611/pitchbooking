package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.dto.GroupSpecificDTO;
import com.hoangthien.pitchbooking.entities.ChildPitch;
import com.hoangthien.pitchbooking.entities.GroupSpecificPitches;
import com.hoangthien.pitchbooking.entities.Pitch;
import com.hoangthien.pitchbooking.entities.PitchType;
import com.hoangthien.pitchbooking.exception.PitchBookingException;
import com.hoangthien.pitchbooking.repositories.ChildPitchRepository;
import com.hoangthien.pitchbooking.repositories.GroupSpecificPitchesRepository;
import com.hoangthien.pitchbooking.repositories.PitchRepository;
import com.hoangthien.pitchbooking.repositories.PitchTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupSpecificPitchesServiceImpl implements GroupSpecificPitchesService {

    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private PitchTypeRepository pitchTypeRepository;

    @Autowired
    private GroupSpecificPitchesRepository groupSpecificPitchesRepository;

    @Autowired
    private ChildPitchRepository childPitchRepository;

    @Override
    public GroupSpecificDTO create(GroupSpecificDTO groupSpecificDTO) {
        Pitch pitch = pitchRepository
                .findById(groupSpecificDTO.getPitchId())
                .orElseThrow(() -> new PitchBookingException("Pitch not found"));

        PitchType pitchType = pitchTypeRepository
                .findById(groupSpecificDTO.getPitchTypeId())
                .orElseThrow(() -> new PitchBookingException("Pitch type not found"));

        GroupSpecificPitches groupSpecificPitches = new GroupSpecificPitches();
        groupSpecificPitches.setPitch(pitch);
        groupSpecificPitches.setPitchType(pitchType);

        GroupSpecificPitches savedGroupSpecificPitches = groupSpecificPitchesRepository.save(groupSpecificPitches);

        List<ChildPitch> childPitches = generateChildPitch(pitchType.getName(), groupSpecificDTO.getNumber());
        childPitches.forEach(childPitch -> childPitch.setGroupSpecificPitches(groupSpecificPitches));
        List<ChildPitch> savedChildPitches = childPitchRepository.saveAll(childPitches);

        groupSpecificDTO.setPitchTypeName(pitchType.getName());
        groupSpecificDTO.setId(savedGroupSpecificPitches.getId());
        groupSpecificDTO.setNumber(savedChildPitches.size());
        return groupSpecificDTO;
    }

    @Transactional
    @Override
    public List<GroupSpecificPitches> getAllByPitchId(Long pitchId) {
        return groupSpecificPitchesRepository.findAllByPitchId(pitchId);
    }

    @Override
    public GroupSpecificDTO delete(Long id) {
        GroupSpecificPitches groupSpecificPitches = groupSpecificPitchesRepository
                .findById(id)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy dữ liệu cần xóa!"));

        groupSpecificPitchesRepository.deleteById(id);

        GroupSpecificDTO groupSpecificDTO = new GroupSpecificDTO();
        groupSpecificDTO.setPitchTypeName(groupSpecificPitches.getPitchType().getName());
        groupSpecificDTO.setPitchTypeId(groupSpecificPitches.getPitchType().getId());
        return groupSpecificDTO;
    }

    @Override
    public GroupSpecificDTO changeNumber(Long id, int number) {
        GroupSpecificPitches groupSpecificPitches = groupSpecificPitchesRepository
                .findById(id)
                .orElseThrow(() -> new PitchBookingException("Không tìm thấy dữ liệu cần thay đổi!"));

        List<ChildPitch> childPitches = generateChildPitch(groupSpecificPitches.getPitchType().getName(), number);
        childPitches.forEach(childPitch -> childPitch.setGroupSpecificPitches(groupSpecificPitches));

        groupSpecificPitches.getChildPitches().clear();
        groupSpecificPitches.getChildPitches().addAll(childPitches);
        GroupSpecificPitches updatedGroupSpecificPitches = groupSpecificPitchesRepository
                .save(groupSpecificPitches);

        GroupSpecificDTO groupSpecificDTO = new GroupSpecificDTO();
        groupSpecificDTO.setPitchTypeName(updatedGroupSpecificPitches.getPitchType().getName());
        groupSpecificDTO.setId(updatedGroupSpecificPitches.getId());
        groupSpecificDTO.setNumber(updatedGroupSpecificPitches.getChildPitches().size());
        return groupSpecificDTO;
    }

    private List<ChildPitch> generateChildPitch(String pitchType, int number) {
        List<ChildPitch> childPitches = new ArrayList<>();
        String typeName = pitchType.contains(" ") ? pitchType.split(" ", 2)[0] : pitchType;
        String name;
        for (int i = 1; i <= number; i++) {
            name = "Sân " + typeName + " số " + i;
            ChildPitch childPitch = new ChildPitch();
            childPitch.setName(name);
            childPitches.add(childPitch);
        }

        return childPitches;
    }
}
