package kr.megaptera.smash.dtos;

public class PlaceDto {
  private final Long id;

  private final Long postId;

  private final String name;

  public PlaceDto(Long id, Long postId, String name) {
    this.id = id;
    this.postId = postId;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public Long getPostId() {
    return postId;
  }

  public String getName() {
    return name;
  }
}
