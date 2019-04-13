package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PitchRepository extends JpaRepository<Pitch, Long> {
}
