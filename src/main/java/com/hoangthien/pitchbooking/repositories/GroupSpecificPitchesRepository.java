package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.GroupSpecificPitches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupSpecificPitchesRepository extends JpaRepository<GroupSpecificPitches, Long> {
    List<GroupSpecificPitches> findAllByPitchId(Long pitchId);
}
