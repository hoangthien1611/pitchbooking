package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Pitch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitchRepository extends JpaRepository<Pitch, Long> {

    List<Pitch> findAllByOwnerId(Long ownerId);

    Page<Pitch> findAll(Pageable pageable);

    Page<Pitch> findAllByDistrictPath(String path, Pageable pageable);
}
