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

  private Long gameId;

  private String name;

  public Place() {

  }

  public Place(Long id,
               Long gameId,
               String name) {
    this.id = id;
    this.gameId = gameId;
    this.name = name;
  }

  public Long id() {
    return id;
  }

  public Long gameId() {
    return gameId;
  }

  public String name() {
    return name;
  }

  public PlaceDto toDto() {
    return new PlaceDto(
        id,
        gameId,
        name
    );
  }
}
