package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
  List<Team> findAllByGameId(Long id);
}
