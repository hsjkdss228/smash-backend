package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.register.Register;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    List<Register> findByGameId(Long gameId);
    List<Register> findAllByGameIdAndUserId(Long gameId, Long userId);
    List<Register> findAllByGameId(Long gameId);
    void deleteAllByGameId(Long gameId);
}
