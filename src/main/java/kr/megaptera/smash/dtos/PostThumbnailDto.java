package kr.megaptera.smash.dtos;

public class PostThumbnailDto {
  private final Long id;

  private final String author;

  private final String detail;

  public PostThumbnailDto(Long id, String author, String detail) {
    this.id = id;
    this.author = author;
    this.detail = detail;
  }

  public Long getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getDetail() {
    return detail;
  }
}
