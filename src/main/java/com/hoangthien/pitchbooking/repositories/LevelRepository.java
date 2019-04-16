package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    @Query("select lv from Level lv join lv.teams t join t.area a where a.id = ?1")
    List<Level> findAllByAreaId(Long areaId);
}
