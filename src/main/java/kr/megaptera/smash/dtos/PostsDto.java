package kr.megaptera.smash.dtos;

import java.util.List;

public class PostsDto {
  private final List<PostListDto> posts;

  public PostsDto(List<PostListDto> posts) {
    this.posts = posts;
  }

  public List<PostListDto> getPosts() {
    return posts;
  }
}
