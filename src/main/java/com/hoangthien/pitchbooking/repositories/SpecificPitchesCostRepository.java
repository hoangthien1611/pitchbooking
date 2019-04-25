package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.SpecificPitchesCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificPitchesCostRepository extends JpaRepository<SpecificPitchesCost, Long> {
}
