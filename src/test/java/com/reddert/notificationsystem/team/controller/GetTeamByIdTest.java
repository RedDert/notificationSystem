package com.reddert.notificationsystem.team.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reddert.notificationsystem.team.dtos.TeamDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GetTeamByIdTest extends BaseTeamControllerTest {

  @Test
  void shouldReturnTeamWhenGetById() throws Exception {
    TeamDTO teamDTO = new TeamDTO(teamId, teamName, teamDescription);

    when(teamService.getTeamById(teamId)).thenReturn(teamDTO);

    mockMvc
        .perform(get("/api/teams/{teamId}", teamId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(teamId.toString()))
        .andExpect(jsonPath("$.name").value(teamName))
        .andExpect(jsonPath("$.description").value(teamDescription));

    verify(teamService, times(1)).getTeamById(teamId);
  }

  @Test
  void shouldReturn404ForNonExistingTeam() throws Exception {
    UUID teamId = UUID.randomUUID();

    when(teamService.getTeamById(teamId))
        .thenThrow(new EntityNotFoundException("Team with ID " + teamId + " not found"));

    mockMvc.perform(get("/api/teams/{teamId}", teamId)).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn400ForInvalidUUID() throws Exception {
    mockMvc.perform(get("/api/teams/{teamId}", "invalid-uuid")).andExpect(status().isBadRequest());
  }
}
