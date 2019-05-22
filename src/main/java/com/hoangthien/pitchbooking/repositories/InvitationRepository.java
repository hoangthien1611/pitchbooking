package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findAllByUserSenderUserName(String userName);
}
