package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findAllByPostId(Long postId);
}
