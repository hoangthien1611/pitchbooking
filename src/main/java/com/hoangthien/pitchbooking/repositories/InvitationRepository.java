package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.Invitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @Query("select i from Invitation i join i.exchange e join e.userCreated uc join i.user u where uc.userName = ?1 or u.userName = ?1 order by e.timeExchange desc ")
    List<Invitation> findAllByUserUserName(String userName);

    @Query("select i from Invitation i join i.exchange e join e.team et join i.team it where (et.id = ?1 or it.id = ?1) and  i.status = 1 and e.timeExchange > ?2 order by e.timeExchange asc")
    Page<Invitation> findByTeamParticipate(Long teamId, LocalDateTime now, Pageable pageable);
}
