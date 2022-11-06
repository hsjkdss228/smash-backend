package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.ImageDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {
  @Id
  @GeneratedValue
  private Long id;

  private Long postId;

  private String url;

  private Boolean isThumbnailImage;

  public Image() {

  }

  public Image(Long id, Long postId, String url, Boolean isThumbnailImage) {
    this.id = id;
    this.postId = postId;
    this.url = url;
    this.isThumbnailImage = isThumbnailImage;
  }

  public String url() {
    return url;
  }

  public ImageDto toDto() {
    return new ImageDto(
        id,
        postId,
        url,
        isThumbnailImage
    );
  }
}
