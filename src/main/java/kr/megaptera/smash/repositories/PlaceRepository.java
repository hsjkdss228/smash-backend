package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PlaceRepository
    extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {
    Optional<Place> findByInformationName(String placeName);
}
