package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
