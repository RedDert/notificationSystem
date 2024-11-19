package com.reddert.notificationsystem.team.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  @Column(name = "role_type")
  private RoleType roleType;

  public Role() {
  }

  public Role(RoleType roleType) {
    this.roleType = roleType;
  }

  public UUID getId() {
    return id;
  }

  public RoleType getRoleType() {
    return roleType;
  }

  public void setRoleType(RoleType roleType) {
    this.roleType = roleType;
  }

}
