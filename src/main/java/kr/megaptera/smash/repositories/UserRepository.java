package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
