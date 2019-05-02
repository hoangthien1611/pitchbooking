package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.SpecificPitchesCost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SpecificPitchesCostRepository extends JpaRepository<SpecificPitchesCost, Long> {

    @Transactional
    @Modifying
    @Query("delete from SpecificPitchesCost cost where cost.id = ?1")
    void deleteById(Long id);

    @Query("select distinct spc.cost from SpecificPitchesCost spc join spc.groupSpecificPitches gsp join gsp.pitch p join p.district d where d.path = ?1")
    List<Integer> findAllDistinctCostsByDistrictPath(String path);

    @Query("select distinct spc.cost from SpecificPitchesCost spc")
    List<Integer> findAllDistinctCosts(Pageable pageable);
}
