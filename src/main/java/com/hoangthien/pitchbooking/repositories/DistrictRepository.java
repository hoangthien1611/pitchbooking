package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findFirstByPath(String path);

    @Query("select distinct d from District d join d.teams t join t.level l where l.id in ?1 and (LOWER(t.name) like %?2% or LOWER(t.time) like %?2%)")
    List<District> findAllByLevelIdInAndSearchTeam(List<Long> levelIds, String searchTeam);

    @Query("select distinct d from District d join d.teams t join t.level l where l.id in ?1")
    List<District> findAllByLevelIdIn(List<Long> levelIds);
}
