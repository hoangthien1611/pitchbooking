package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.YardSurface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YardSurfaceRepository extends JpaRepository<YardSurface, Long> {
}
