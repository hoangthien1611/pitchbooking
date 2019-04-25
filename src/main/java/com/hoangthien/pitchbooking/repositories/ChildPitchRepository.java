package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.ChildPitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildPitchRepository extends JpaRepository<ChildPitch, Long> {
}
