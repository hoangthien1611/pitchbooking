package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    @Query("select distinct lv from Level lv join lv.teams t join t.area a where a.id = ?1")
    List<Level> findAllByAreaId(Long areaId);

    @Query("select distinct lv from Level lv join lv.teams t where LOWER(t.name) like %?1% or LOWER(t.time) like %?1%")
    List<Level> findAllByTeamSearch(String teamSearch);

    @Query("select distinct lv from Level lv join lv.teams t join t.area a where a.id = ?1 and (LOWER(t.name) like %?2% or LOWER(t.time) like %?2%)")
    List<Level> findAllByAreaIdAndTeamSearch(Long areaId, String teamSearch);

    @Query("select distinct lv from Level lv join lv.exchanges e join e.district d where d.path = ?1")
    List<Level> findAllByDistrictPath(String path);

    @Query("select distinct lv from Level lv join lv.exchanges e join e.team t where LOWER(e.bet) like %?1% or lower(e.area) like %?1% or lower(t.name) like %?1%")
    List<Level> findAllByExchangeSearch(String search);

    @Query("select distinct lv from Level lv join lv.exchanges e join e.district d join e.team t where d.path = ?1 and (LOWER(e.bet) like %?2% or lower(e.area) like %?2% or lower(t.name) like %?2%)")
    List<Level> findAllByDistrictPathAndExchangeSearch(String path, String search);
}
