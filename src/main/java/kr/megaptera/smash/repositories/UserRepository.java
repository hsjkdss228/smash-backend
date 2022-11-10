package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Person, Long> {

}
