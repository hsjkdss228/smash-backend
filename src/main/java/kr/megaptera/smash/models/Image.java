package kr.megaptera.smash.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kr.megaptera.smash.dtos.ImageDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
