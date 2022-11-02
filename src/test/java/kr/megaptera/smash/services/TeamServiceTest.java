package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Team;
import kr.megaptera.smash.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TeamServiceTest {
  private TeamService teamService;
  private TeamRepository teamRepository;

  @BeforeEach
  void setUp() {
    teamRepository = mock(TeamRepository.class);
    teamService = new TeamService(teamRepository);
  }

  @Test
  void teams() {
    List<Team> teams = List.of(
        new Team(1L, 1L, "Team 1",
            "Baseball", "11월 1일 18:00 ~ 21:00",
            "Practice", "Amateur", "Male",
            12, 10000),
        new Team(2L, 2L, "Team 1",
            "Soccer", "11월 2일 15:00 ~ 17:00",
            "Actual Game", "Professional", "mixed",
            6, 5000)
    );
    given(teamRepository.findAll()).willReturn(teams);

    List<Team> foundTeams = teamService.teams();

    assertThat(foundTeams).isNotNull();
    assertThat(foundTeams).hasSize(2);

    verify(teamRepository).findAll();
  }
}






















