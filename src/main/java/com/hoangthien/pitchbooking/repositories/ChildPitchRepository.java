package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.ChildPitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildPitchRepository extends JpaRepository<ChildPitch, Long> {
    void deleteAllByGroupSpecificPitchesId(Long id);

    @Query("select cp from ChildPitch cp join cp.groupSpecificPitches gsp join gsp.pitch p where p.id = ?1 order by cp.name asc")
    List<ChildPitch> findAllByPitchId(Long id);
}
