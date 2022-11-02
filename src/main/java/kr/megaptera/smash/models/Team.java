package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.TeamDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Team {
  @Id
  @GeneratedValue
  private Long id;

  private Long postId;

  private String name;

  private String exercise;

  private String exerciseDate;

  private String exerciseType;

  private String exerciseLevel;

  private String exerciseGender;

  private Integer targetMembersCount;

  private Integer cost;

  public Team() {

  }

  public Team(Long id,
              Long postId,
              String name,
              String exercise,
              String exerciseDate,
              String exerciseType,
              String exerciseLevel,
              String exerciseGender,
              Integer targetMembersCount,
              Integer cost) {
    this.id = id;
    this.postId = postId;
    this.name = name;
    this.exercise = exercise;
    this.exerciseDate = exerciseDate;
    this.exerciseType = exerciseType;
    this.exerciseLevel = exerciseLevel;
    this.exerciseGender = exerciseGender;
    this.targetMembersCount = targetMembersCount;
    this.cost = cost;
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

  public String exercise() {
    return exercise;
  }

  public String exerciseDate() {
    return exerciseDate;
  }

  public String exerciseType() {
    return exerciseType;
  }

  public String exerciseLevel() {
    return exerciseLevel;
  }

  public String exerciseGender() {
    return exerciseGender;
  }

  public Integer targetMembersCount() {
    return targetMembersCount;
  }

  public Integer cost() {
    return cost;
  }

  public TeamDto toDto(Integer membersCount) {
    return new TeamDto(
        id,
        postId,
        name,
        exercise,
        exerciseDate,
        exerciseType,
        exerciseLevel,
        exerciseGender,
        membersCount,
        targetMembersCount,
        cost
    );
  }
}
