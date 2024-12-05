package com.reddert.notificationsystem.team.dtos;

public class RoleChangeResponseDTO {
  private UserMembershipDTO targetUser;
  private UserMembershipDTO requesterUser;

  public RoleChangeResponseDTO(UserMembershipDTO targetUser, UserMembershipDTO requesterUser) {
    this.targetUser = targetUser;
    this.requesterUser = requesterUser;
  }

  public UserMembershipDTO getTargetUser() {
    return targetUser;
  }

  public void setTargetUser(UserMembershipDTO targetUser) {
    this.targetUser = targetUser;
  }

  public UserMembershipDTO getRequesterUser() {
    return requesterUser;
  }

  public void setRequesterUser(UserMembershipDTO requesterUser) {
    this.requesterUser = requesterUser;
  }
}
