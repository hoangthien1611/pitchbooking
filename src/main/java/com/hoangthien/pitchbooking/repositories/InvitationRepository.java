package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @Query("select i from Invitation i join i.exchange e join e.userCreated uc join i.user u where uc.userName = ?1 or u.userName = ?1 order by e.timeExchange desc ")
    List<Invitation> findAllByUserUserName(String userName);
}
