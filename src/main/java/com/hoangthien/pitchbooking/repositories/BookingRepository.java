package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select distinct b from Booking b join b.childPitch cp join cp.groupSpecificPitches gsp join gsp.specificPitchesCosts spc where spc.id = ?1 and b.dateBooking = ?2")
    List<Booking> findAllByPitchesCostIdAndDateBooking(Long pitchesCostId, LocalDate date);

    Optional<Booking> findFirstByDateBookingAndFromTimeAndToTimeAndChildPitchId(LocalDate localDate, String fromTime, String toTime, Long childPitchId);
}
