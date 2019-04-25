package com.hoangthien.pitchbooking.services;

import com.hoangthien.pitchbooking.entities.GroupDays;
import com.hoangthien.pitchbooking.repositories.GroupDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupDaysServiceImpl implements GroupDaysService {

    @Autowired
    private GroupDaysRepository groupDaysRepository;

    @Override
    public List<GroupDays> getAll() {
        return groupDaysRepository.findAll();
    }
}
