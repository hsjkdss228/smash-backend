package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PostDto;
import kr.megaptera.smash.dtos.PostNotFoundErrorDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.PostNotFound;
import kr.megaptera.smash.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/posts/list")
  public PostsDto posts() {
    return postService.posts();
  }

  @GetMapping("/posts/{postId}")
  public PostDto post(
      @PathVariable Long postId,
      @RequestAttribute("userId") Long accessedUserId
  ) {
    return postService.post(postId, accessedUserId);
  }

  @ExceptionHandler(PostNotFound.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public PostNotFoundErrorDto postNotFound(PostNotFound exception) {
    return new PostNotFoundErrorDto(exception.getMessage());
  }
}