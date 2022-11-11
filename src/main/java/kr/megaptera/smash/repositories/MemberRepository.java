package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByGameId(Long gameId);
}
