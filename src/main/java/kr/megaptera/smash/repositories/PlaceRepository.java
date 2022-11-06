package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
  Place findByGameId(Long id);
}
