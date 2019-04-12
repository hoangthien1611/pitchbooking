package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByPath(String path);
}
