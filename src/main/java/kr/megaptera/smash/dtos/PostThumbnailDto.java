package kr.megaptera.smash.dtos;

import java.util.List;

public class PostThumbnailDto {
  private final Long id;

  private final String postType;

  private final String author;

  private final String createdAt;

  private final Integer hits;

  private final List<ImageDto> images;

  private final String detail;

  public PostThumbnailDto(Long id,
                          String postType,
                          String author,
                          String createdAt,
                          Integer hits,
                          List<ImageDto> images,
                          String detail) {
    this.id = id;
    this.postType = postType;
    this.author = author;
    this.createdAt = createdAt;
    this.hits = hits;
    this.images = images;
    this.detail = detail;
  }

  public Long getId() {
    return id;
  }

  public String getPostType() {
    return postType;
  }

  public String getAuthor() {
    return author;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public Integer getHits() {
    return hits;
  }

  public List<ImageDto> getImages() {
    return images;
  }

  public String getDetail() {
    return detail;
  }
}
