package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.ImageDto;
import kr.megaptera.smash.dtos.PostThumbnailDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Post {
  @Id
  @GeneratedValue
  private Long id;

  private Long userId;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  private String type;

  private Integer hits;

  private String detail;

  public Post() {

  }

  public Post(Long id,
              Long userId,
              String type,
              Integer hits,
              String detail) {
    this.id = id;
    this.userId = userId;
    this.type = type;
    this.hits = hits;
    this.detail = detail;
  }

  public Long id() {
    return id;
  }

  public Long userId() {
    return userId;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public String type() {
    return type;
  }

  public Integer hits() {
    return hits;
  }

  public String detail() {
    return detail;
  }

  public String convertDateTime() {
    return "";
//    return createdAt.format();
  }

  public PostThumbnailDto toThumbnailDto(String author,
                                         List<ImageDto> imageDtos) {
    return new PostThumbnailDto(
        id,
        type,
        author,
        convertDateTime(),
        hits,
        imageDtos,
        detail
    );
  }
}
