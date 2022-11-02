package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public List<Post> posts() {
    return postRepository.findAll();
  }
}
