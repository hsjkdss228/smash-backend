package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostServiceTest {
  private PostService postService;
  private PostRepository postRepository;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    postService = new PostService(postRepository);
  }

  @Test
  void posts() {
    List<Post> posts = List.of(
        // id, userId
        new Post(1L, 1L, "Gathering", 15, "Play baseball with me"),
        new Post(2L, 2L, "Gathering", 30, "Play football with me")
    );
    given(postRepository.findAll()).willReturn(posts);

    List<Post> foundPosts = postService.posts();

    assertThat(foundPosts).isNotNull();

    verify(postRepository).findAll();
  }
}
