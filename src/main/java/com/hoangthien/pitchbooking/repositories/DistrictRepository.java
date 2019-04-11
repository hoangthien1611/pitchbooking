package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
}
