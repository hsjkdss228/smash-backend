package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Team;
import kr.megaptera.smash.repositories.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeamService {
  private final TeamRepository teamRepository;

  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  public List<Team> teams() {
    return teamRepository.findAll();
  }
}
