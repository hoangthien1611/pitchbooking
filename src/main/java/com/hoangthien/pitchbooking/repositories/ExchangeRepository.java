package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    @Query("select distinct e from Exchange e join e.district d join e.level l join e.team t where d.path = ?1 and e.hasPitch in ?2 and l.id in ?3 and (LOWER(e.bet) like %?4% or lower(e.area) like %?4% or lower(t.name) like %?4%) and e.timeExchange > ?5")
    Page<Exchange> findAllByDistrictPathAndHasPitchInAndLevelIdInAndSearchAndTimeExchangeAfter(String path, List<Integer> hasPitch, List<Long> levelIds, String search, LocalDateTime now, Pageable pageable);

    @Query("select distinct e from Exchange e join e.district d join e.level l where d.path = ?1 and e.hasPitch in ?2 and l.id in ?3 and e.timeExchange > ?4")
    Page<Exchange> findAllByDistrictPathAndHasPitchInAndLevelIdInAndTimeExchangeAfter(String path, List<Integer> hasPitch, List<Long> levelIds, LocalDateTime now, Pageable pageable);

    @Query("select distinct e from Exchange e join e.district d join e.level l join e.team t where e.hasPitch in ?1 and l.id in ?2 and (LOWER(e.bet) like %?3% or lower(e.area) like %?3% or lower(t.name) like %?3%) and e.timeExchange > ?4")
    Page<Exchange> findAllByHasPitchInAndLevelIdInAndSearchAndTimeExchangeAfter(List<Integer> hasPitch, List<Long> levelIds, String search, LocalDateTime now, Pageable pageable);

    @Query("select distinct e from Exchange e join e.district d join e.level l where e.hasPitch in ?1 and l.id in ?2 and e.timeExchange > ?3")
    Page<Exchange> findAllByHasPitchInAndLevelIdInAndTimeExchangeAfter(List<Integer> hasPitch, List<Long> levelIds, LocalDateTime now, Pageable pageable);
}
