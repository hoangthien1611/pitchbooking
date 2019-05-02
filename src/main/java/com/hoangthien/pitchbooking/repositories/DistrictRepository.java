package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findFirstByPath(String path);
}
