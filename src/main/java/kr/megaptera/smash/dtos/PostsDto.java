package kr.megaptera.smash.dtos;

import java.util.List;

public class PostsDto {
  private final List<PostDto> posts;

  public PostsDto(List<PostDto> posts) {
    this.posts = posts;
  }

  public List<PostDto> getPosts() {
    return posts;
  }
}
