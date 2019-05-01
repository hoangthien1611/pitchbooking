package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
