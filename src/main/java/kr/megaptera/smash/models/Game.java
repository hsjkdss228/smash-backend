package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameDto;
import kr.megaptera.smash.dtos.TeamDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Game {
  @Id
  @GeneratedValue
  private Long id;

  private Long postId;

  private String exercise;

  private String exerciseDate;

  private String exerciseType;

  private String exerciseLevel;

  private String exerciseGender;

  private Integer cost;

  public Game() {

  }

  public Game(Long id,
              Long postId,
              String exercise,
              String exerciseDate,
              String exerciseType,
              String exerciseLevel,
              String exerciseGender,
              Integer cost
  ) {
    this.id = id;
    this.postId = postId;
    this.exercise = exercise;
    this.exerciseDate = exerciseDate;
    this.exerciseType = exerciseType;
    this.exerciseLevel = exerciseLevel;
    this.exerciseGender = exerciseGender;
    this.cost = cost;
  }

  public Long id() {
    return id;
  }

  public GameDto toDto(
      String place,
      List<TeamDto> teams
  ) {
    return new GameDto(
        id,
        postId,
        exercise,
        exerciseDate,
        exerciseType,
        exerciseLevel,
        exerciseGender,
        cost,
        place,
        teams
    );
  }

  public GameDto toDto(
      String place,
      List<TeamDto> teams,
      String userStatus,
      Long roleIdOfAccessedUser
  ) {
    return new GameDto(
        id,
        postId,
        exercise,
        exerciseDate,
        exerciseType,
        exerciseLevel,
        exerciseGender,
        cost,
        place,
        teams,
        userStatus,
        roleIdOfAccessedUser
    );
  }
}
