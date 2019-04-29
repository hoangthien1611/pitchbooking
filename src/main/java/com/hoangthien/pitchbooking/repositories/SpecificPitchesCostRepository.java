package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.SpecificPitchesCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SpecificPitchesCostRepository extends JpaRepository<SpecificPitchesCost, Long> {

    @Transactional
    @Modifying
    @Query("delete from SpecificPitchesCost cost where cost.id = ?1")
    void deleteById(Long id);
}
