package com.reddert.notificationsystem.team.model;

import com.reddert.notificationsystem.user.model.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_of")
public class MemberOf {

  @EmbeddedId
  private MemberOfId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("teamId")
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  public MemberOfId getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Team getTeam() {
    return team;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public MemberOf() {
  }

  public MemberOf(User user, Team team, Role role) {
    this.user = user;
    this.team = team;
    this.role = role;
    this.id = new MemberOfId(user.getId(), team.getId());
  }

}

