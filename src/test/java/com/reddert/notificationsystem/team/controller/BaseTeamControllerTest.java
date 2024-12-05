package com.reddert.notificationsystem.team.controller;

import com.reddert.notificationsystem.team.service.TeamService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeamController.class)
public abstract class BaseTeamControllerTest {

  @Autowired protected MockMvc mockMvc;

  @MockBean protected TeamService teamService;

  protected UUID teamId;
  protected String teamName = "Team A";
  protected String teamDescription = "Description A";

  @BeforeEach
  void setup() {
    teamId = UUID.randomUUID();
    MockitoAnnotations.openMocks(this);
  }
}
