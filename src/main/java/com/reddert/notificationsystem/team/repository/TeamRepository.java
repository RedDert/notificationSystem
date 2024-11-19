package com.reddert.notificationsystem.team.repository;

import com.reddert.notificationsystem.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
}
