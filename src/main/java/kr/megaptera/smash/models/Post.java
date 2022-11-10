package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Post {
  @Id
  @GeneratedValue
  private Long id;

  private Long hits;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  private Post() {

  }

  public Post(Long id,
              Long hits
  ) {
    this.id = id;
    this.hits = hits;
  }

  public Post(Long id,
              Long hits,
              LocalDateTime createdAt,
              LocalDateTime updatedAt
  ) {
    this.id = id;
    this.hits = hits;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long id() {
    return id;
  }

  public Long hits() {
    return hits;
  }

  // TODO: 알아볼 수 없는 값들을 값 객체로 정의

  public static Post fake(Long id) {
    return new Post(
        id,
        100L,
        LocalDateTime.now(),
        LocalDateTime.now()
    );
  }

  public PostListDto toPostListDto(GameInPostListDto gameInPostListDto) {
    return new PostListDto(
        id,
        hits,
        gameInPostListDto
    );
  }
}
