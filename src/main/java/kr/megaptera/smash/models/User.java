package kr.megaptera.smash.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "PERSON")
public class User {
  @Id
  @GeneratedValue
  private Long personId;

  private String name;

  @OneToMany(mappedBy = "author")
  private List<Post> createdPosts;

  @OneToOne(mappedBy = "person")
  private Member member;

  public User() {

  }

  public User(String name) {
    this.name = name;
  }

  public String author() {
    return name;
  }
}
