package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.PitchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PitchTypeRepository extends JpaRepository<PitchType, Long> {
}
