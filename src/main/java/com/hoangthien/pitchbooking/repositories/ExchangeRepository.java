package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}
