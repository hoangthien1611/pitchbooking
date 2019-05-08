package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByPath(String path);

    Optional<Team> findByPath(String path);

    Page<Team> findAllByAreaIdAndLevelId(Long areaId, Long levelId, Pageable pageable);

    Page<Team> findAllByAreaId(Long areaId, Pageable pageable);

    Page<Team> findAllByLevelId(Long levelId, Pageable pageable);

    Page<Team> findAll(Pageable pageable);

    @Query("select distinct t from Team t join t.level l where l.id in ?1 and (LOWER(t.name) like %?2% or LOWER(t.time) like %?2%)")
    Page<Team> findAllByLevelIdInAndSearch(List<Long> levelIds, String search, Pageable pageable);

    @Query("select distinct t from Team t join t.level l join t.area a where a.id = ?1 and l.id in ?2 and (lower(t.name) like %?3% or lower(t.time) like %?3%)")
    Page<Team> findAllByAreaIdAndLevelIdInAndSearch(Long areaId, List<Long> levelIds, String search, Pageable pageable);

    Page<Team> findAllByLevelIdIn(List<Long> levelIds, Pageable pageable);

    Page<Team> findAllByAreaIdAndLevelIdIn(Long areaId, List<Long> levelIds, Pageable pageable);
}
