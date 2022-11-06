package kr.megaptera.smash.dtos;

import java.util.List;

public class PostDto {
  private final Long id;

  private final String type;

  private final String author;

  private final String createdAt;

  private final Integer hits;

  private final String detail;

  private final List<ImageDto> images;

  private final GameDto game;

  public PostDto(Long id,
                 String type,
                 String author,
                 String createdAt,
                 Integer hits,
                 String detail,
                 List<ImageDto> images,
                 GameDto game)
  {
    this.id = id;
    this.type = type;
    this.author = author;
    this.createdAt = createdAt;
    this.hits = hits;
    this.detail = detail;
    this.images = images;
    this.game = game;
  }

  public Long getId() {
    return id;
  }

  public String getType() {
    return type;
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

  public String getDetail() {
    return detail;
  }

  public List<ImageDto> getImages() {
    return images;
  }

  public GameDto getGame() {
    return game;
  }
}
