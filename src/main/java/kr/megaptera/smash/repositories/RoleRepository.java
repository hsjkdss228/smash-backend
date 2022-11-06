package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
  List<Role> findAllByGameId(Long id);
}
