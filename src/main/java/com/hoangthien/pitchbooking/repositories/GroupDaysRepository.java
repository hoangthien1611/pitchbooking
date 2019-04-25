package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.GroupDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDaysRepository extends JpaRepository<GroupDays, Long> {
}
