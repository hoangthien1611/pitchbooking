package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByPath(String path);

    Optional<Team> findByPath(String path);

    List<Team> findAllByAreaIdAndLevelId(Long areaId, Long levelId, Pageable pageable);

    List<Team> findAllByAreaId(Long areaId, Pageable pageable);
}
