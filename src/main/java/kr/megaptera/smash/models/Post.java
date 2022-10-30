package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.PostThumbnailDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Post {
  @Id
  @GeneratedValue
  private Long id;

  @OneToMany(mappedBy = "postIn")
  private List<Team> teamsUnder;

  @ManyToOne
  @JoinColumn(name = "AUTHOR_ID")
  private User author;

  private String detail;

  public Post() {

  }

  public Post(Long id) {
    this.id = id;
  }

  public Post(Long id, User author, String detail) {
    this.id = id;
    this.author = author;
    this.detail = detail;
  }

  public PostThumbnailDto toThumbnailDto() {
    return new PostThumbnailDto(id, author.author(), detail);
  }

  public Long id() {
    return id;
  }
}
