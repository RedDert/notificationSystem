
package com.reddert.notificationsystem.team.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddert.notificationsystem.team.model.Role;
import com.reddert.notificationsystem.team.model.RoleType;

public interface RoleRepository extends JpaRepository<Role, UUID> {
   Optional<Role> findByRoleType(RoleType roleType);
}
