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

  private Double mannerScore;

  // TODO: 성별이 추가되어야 함

  public User() {

  }

  public User(String name) {
    this.name = name;
  }

  public User(Long id, String name, Double mannerScore) {
    this.id = id;
    this.name = name;
    this.mannerScore = mannerScore;
  }

  public String name() {
    return name;
  }

  public Double mannerScore() {
    return mannerScore;
  }
}
