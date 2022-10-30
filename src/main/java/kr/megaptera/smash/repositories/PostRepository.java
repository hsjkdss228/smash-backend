package kr.megaptera.smash.repositories;

import kr.megaptera.smash.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
