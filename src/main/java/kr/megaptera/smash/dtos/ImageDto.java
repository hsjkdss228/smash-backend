package kr.megaptera.smash.dtos;

public class ImageDto {
  private final Long id;

  private final Long postId;

  private final String url;

  private final Boolean isThumbnailImage;

  public ImageDto(Long id, Long postId, String url, Boolean isThumbnailImage) {
    this.id = id;
    this.postId = postId;
    this.url = url;
    this.isThumbnailImage = isThumbnailImage;
  }

  public Long getId() {
    return id;
  }

  public Long getPostId() {
    return postId;
  }

  public String getUrl() {
    return url;
  }

  public Boolean getIsThumbnailImage() {
    return isThumbnailImage;
  }
}
