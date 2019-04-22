package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByPath(String path);

    Optional<Team> findByPath(String path);

    Page<Team> findAllByAreaIdAndLevelId(Long areaId, Long levelId, Pageable pageable);

    Page<Team> findAllByAreaId(Long areaId, Pageable pageable);

    Page<Team> findAllByLevelId(Long levelId, Pageable pageable);

    Page<Team> findAll(Pageable pageable);
}
