package com.reddert.notificationsystem.team.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class MemberOfId implements Serializable {

  private UUID userId;
  private UUID teamId;

  public MemberOfId() {
  }

  public MemberOfId(UUID userId, UUID teamId) {
    this.userId = userId;
    this.teamId = teamId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    MemberOfId that = (MemberOfId) o;
    return Objects.equals(userId, that.userId) && Objects.equals(teamId, that.teamId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, teamId);
  }
}
