package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountIdentifier(String identifier);
}
