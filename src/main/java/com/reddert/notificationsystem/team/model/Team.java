package com.reddert.notificationsystem.team.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.reddert.notificationsystem.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "team")
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  private String name;

  private String description;

  @NotNull
  private LocalDateTime createdAt;

  public Team(String name) {
    this.name = name;
    this.createdAt = LocalDateTime.now();
  }

  public Team(String name, String description) {
    this.name = name;
    this.description = description;
    this.createdAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
