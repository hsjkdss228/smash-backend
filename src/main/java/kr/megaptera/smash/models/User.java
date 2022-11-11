package kr.megaptera.smash.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERSON")
public class User {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String gender;

  private User() {

  }

  public User(Long id,
              String name,
              String gender
  ) {
    this.id = id;
    this.name = name;
    this.gender = gender;
  }

  public String name() {
    return name;
  }

  public static User fake(Long id) {
    return new User(
        id,
        "유저 이름",
        "성별"
    );
  }
}
