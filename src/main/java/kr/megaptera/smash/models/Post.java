package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameDto;
import kr.megaptera.smash.dtos.ImageDto;
import kr.megaptera.smash.dtos.PostDto;
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

  private Long authorId;

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
    this.authorId = userId;
    this.type = type;
    this.hits = hits;
    this.detail = detail;
  }

  public Long id() {
    return id;
  }

  public Long userId() {
    return authorId;
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

  public PostDto toPostDto(String author,
                           List<ImageDto> images,
                           GameDto game
  ) {
    return new PostDto(
        id,
        type,
        author,
        convertDateTime(),
        hits,
        detail,
        images,
        game
    );
  }
}
