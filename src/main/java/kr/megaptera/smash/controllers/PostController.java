package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PostThumbnailsDto;
import kr.megaptera.smash.services.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/posts/list")
  public PostThumbnailsDto posts() {
    return postService.posts();
  }
}
