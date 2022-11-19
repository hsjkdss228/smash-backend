package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    List<Register> findByGameId(Long gameId);
    Optional<Register> findByGameIdAndUserId(Long gameId, Long userId);
    List<Register> findAllByGameId(Long gameId);
}
