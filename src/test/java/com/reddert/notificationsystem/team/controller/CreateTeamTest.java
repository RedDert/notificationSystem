package com.reddert.notificationsystem.team.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reddert.notificationsystem.team.dtos.CreateTeamDTO;
import com.reddert.notificationsystem.team.dtos.TeamDTO;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class CreateTeamTest extends BaseTeamControllerTest {

  @Test
  void shouldCreateNewTeam() throws Exception {
    UUID teamId = UUID.randomUUID();
    String newTeamName = "New Team";
    String newTeamDescription = "New Description";

    TeamDTO teamDTO = new TeamDTO(teamId, newTeamName, newTeamDescription);

    when(teamService.createTeam(any(CreateTeamDTO.class))).thenReturn(teamDTO);

    mockMvc
        .perform(
            post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Team\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(teamId.toString()))
        .andExpect(jsonPath("$.name").value(newTeamName))
        .andExpect(jsonPath("$.description").value(newTeamDescription));

    verify(teamService, times(1)).createTeam(any(CreateTeamDTO.class));
  }
  /* TODO FIXII

  @Test
  void shouldReturn400WhenTeamNameIsMissing() throws Exception {
    mockMvc
        .perform(post("/api/teams").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }
  */
}
