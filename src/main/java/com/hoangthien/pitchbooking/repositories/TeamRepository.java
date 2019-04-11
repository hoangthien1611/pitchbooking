package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository {
    boolean existsByPath(String path);

    Team save(Team team);
}
