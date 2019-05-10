package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Pitch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitchRepository extends JpaRepository<Pitch, Long> {

    List<Pitch> findAllByOwnerId(Long ownerId);

    Page<Pitch> findAll(Pageable pageable);

    @Query("select distinct p from Pitch p join p.yardSurface ys join p.groupSpecificPitches gsp join gsp.specificPitchesCosts spc join gsp.pitchType pt where pt.id in ?1 and ys.id in ?2 and spc.cost in ?3")
    Page<Pitch> findAllByPitchTypeIdInAndSurfaceIdInAndCostIn(List<Long> typeIds, List<Long> surfaceIds, List<Integer> costs, Pageable pageable);

    @Query("select distinct p from Pitch p join p.yardSurface ys join p.district d join p.groupSpecificPitches gsp join gsp.specificPitchesCosts spc join gsp.pitchType pt where d.path = ?1 and pt.id in ?2 and ys.id in ?3 and spc.cost in ?4")
    Page<Pitch> findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdInAndCostIn(String path, List<Long> typeIds, List<Long> surfaceIds, List<Integer> costs, Pageable pageable);

    @Query("select distinct p from Pitch p join p.yardSurface ys join p.district d join p.groupSpecificPitches gsp join gsp.pitchType pt where d.path = ?1 and pt.id in ?2 and ys.id in ?3")
    Page<Pitch> findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdIn(String path, List<Long> typeIds, List<Long> surfaceIds, Pageable pageable);

    @Query("select distinct p from Pitch p join p.yardSurface ys join p.groupSpecificPitches gsp join gsp.specificPitchesCosts spc join gsp.pitchType pt where pt.id in ?1 and ys.id in ?2 and spc.cost in ?3 and (LOWER(p.name) like %?4% or LOWER(p.address) like %?4%)")
    Page<Pitch> findAllByPitchTypeIdInAndSurfaceIdInAndCostInAndSearch(List<Long> typeIds, List<Long> surfaceIds, List<Integer> costs, String search, Pageable pageable);

    @Query("select distinct p from Pitch p join p.yardSurface ys join p.district d join p.groupSpecificPitches gsp join gsp.specificPitchesCosts spc join gsp.pitchType pt where d.path = ?1 and pt.id in ?2 and ys.id in ?3 and spc.cost in ?4 and (LOWER(p.name) like %?5% or LOWER(p.address) like %?5%)")
    Page<Pitch> findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdInAndCostInAndSearch(String path, List<Long> typeIds, List<Long> surfaceIds, List<Integer> costs, String search, Pageable pageable);

    @Query("select distinct p from Pitch p join p.yardSurface ys join p.district d join p.groupSpecificPitches gsp join gsp.pitchType pt where d.path = ?1 and pt.id in ?2 and ys.id in ?3 and (LOWER(p.name) like %?4% or LOWER(p.address) like %?4%)")
    Page<Pitch> findAllByDistrictPathAndPitchTypeIdInAndSurfaceIdInAndSearch(String path, List<Long> typeIds, List<Long> surfaceIds, String search, Pageable pageable);

    List<Pitch> findAllByDistrictId(Long districtId);
}
