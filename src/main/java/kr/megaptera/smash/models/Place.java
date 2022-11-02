package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.PlaceDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Place {
  @Id
  @GeneratedValue
  private Long id;

  private Long postId;

  private String name;

  public Place() {

  }

  public Place(Long id,
               Long postId,
               String name) {
    this.id = id;
    this.postId = postId;
    this.name = name;
  }

  public Long id() {
    return id;
  }

  public Long postId() {
    return postId;
  }

  public String name() {
    return name;
  }

  public PlaceDto toDto() {
    return new PlaceDto(
        id,
        postId,
        name
    );
  }
}
