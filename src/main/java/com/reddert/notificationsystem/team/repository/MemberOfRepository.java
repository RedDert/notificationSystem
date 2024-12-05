package com.reddert.notificationsystem.team.repository;

import com.reddert.notificationsystem.team.model.MemberOf;
import com.reddert.notificationsystem.team.model.MemberOfId;
import com.reddert.notificationsystem.team.model.RoleType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberOfRepository extends JpaRepository<MemberOf, MemberOfId> {
  List<MemberOf> findAllByUserId(UUID userId);

  List<MemberOf> findAllByTeamId(UUID teamId);

  List<MemberOf> findAllByTeam_IdAndRole_RoleType(UUID teamId, RoleType roleType);

  RoleType findRole_RoleTypeByTeam_IdAndUser_Id(UUID teamId, UUID userId);

  RoleType findRole_RoleTypeById(MemberOfId memberId);

  boolean existsByTeamId(UUID teamId);
}
