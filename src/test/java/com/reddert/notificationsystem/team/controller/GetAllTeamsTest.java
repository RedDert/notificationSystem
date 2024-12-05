package com.reddert.notificationsystem.team.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reddert.notificationsystem.team.dtos.TeamDTO;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GetAllTeamsTest extends BaseTeamControllerTest {

  @Test
  void shouldReturnAllTeams() throws Exception {

    List<TeamDTO> teamList =
        List.of(
            new TeamDTO(UUID.randomUUID(), teamName, teamDescription),
            new TeamDTO(UUID.randomUUID(), "Team B", "Description B"));

    when(teamService.getAllTeams()).thenReturn(teamList);

    mockMvc
        .perform(get("/api/teams"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value(teamName))
        .andExpect(jsonPath("$[0].description").value(teamDescription))
        .andExpect(jsonPath("$[1].name").value("Team B"))
        .andExpect(jsonPath("$[1].description").value("Description B"));
  }
}
