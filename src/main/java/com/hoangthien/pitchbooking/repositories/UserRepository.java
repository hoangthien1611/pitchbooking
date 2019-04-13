package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
