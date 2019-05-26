package com.hoangthien.pitchbooking.repositories;

import com.hoangthien.pitchbooking.entities.UserTeam;
import com.hoangthien.pitchbooking.entities.UserTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamId> {
    @Query("select ut from UserTeam ut join ut.team t join t.captain c where c.userName = ?1 and ut.accepted = false")
    List<UserTeam> findAllByTeamCaptainUserNameAndAcceptedFalse(String userName);

    Optional<UserTeam> findByUserIdAndTeamId(Long userId, Long teamId);
}
