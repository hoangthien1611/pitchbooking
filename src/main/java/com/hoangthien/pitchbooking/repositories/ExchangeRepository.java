package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    @Query("select distinct e from Exchange e join e.district d join e.level l where d.path = ?1 and e.hasPitch in ?2 and l.id in ?3 and (LOWER(e.team) like %?4% or lower(e.area) like %?4%)")
    Page<Exchange> findAllByDistrictPathAndHasPitchInAndLevelIdInAndSearch(String path, List<Integer> hasPitch, List<Long> levelIds, String search, Pageable pageable);

    @Query("select distinct e from Exchange e join e.district d join e.level l where d.path = ?1 and e.hasPitch in ?2 and l.id in ?3")
    Page<Exchange> findAllByDistrictPathAndHasPitchInAndLevelIdIn(String path, List<Integer> hasPitch, List<Long> levelIds, Pageable pageable);

    @Query("select distinct e from Exchange e join e.district d join e.level l where e.hasPitch in ?1 and l.id in ?2 and (LOWER(e.team) like %?3% or lower(e.area) like %?3%)")
    Page<Exchange> findAllByHasPitchInAndLevelIdInAndSearch(List<Integer> hasPitch, List<Long> levelIds, String search, Pageable pageable);

    @Query("select distinct e from Exchange e join e.district d join e.level l where e.hasPitch in ?1 and l.id in ?2")
    Page<Exchange> findAllByHasPitchInAndLevelIdIn(List<Integer> hasPitch, List<Long> levelIds, Pageable pageable);
}
