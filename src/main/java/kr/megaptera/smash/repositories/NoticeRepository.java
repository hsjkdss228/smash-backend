package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Notice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByUserId(Long userId, Sort sort);
    List<Notice> findAllByUserId(Long userId);
}
