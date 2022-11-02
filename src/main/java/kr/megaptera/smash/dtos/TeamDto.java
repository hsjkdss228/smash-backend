package kr.megaptera.smash.dtos;

public class TeamDto {
  private final Long id;

  private final Long postId;

  private final String name;

  private final String exercise;

  private final String exerciseDate;

  private final String exerciseType;

  private final String exerciseLevel;

  private final String exerciseGender;

  private final Integer membersCount;

  private final Integer targetMembersCount;

  private final Integer cost;

  public TeamDto(Long id,
                 Long postId,
                 String name,
                 String exercise,
                 String exerciseDate,
                 String exerciseType,
                 String exerciseLevel,
                 String exerciseGender,
                 Integer membersCount,
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
    this.membersCount = membersCount;
    this.targetMembersCount = targetMembersCount;
    this.cost = cost;
  }

  public Long getId() {
    return id;
  }

  public Long getPostId() {
    return postId;
  }

  public String getName() {
    return name;
  }

  public String getExercise() {
    return exercise;
  }

  public String getExerciseDate() {
    return exerciseDate;
  }

  public String getExerciseType() {
    return exerciseType;
  }

  public String getExerciseLevel() {
    return exerciseLevel;
  }

  public String getExerciseGender() {
    return exerciseGender;
  }

  public Integer getMembersCount() {
    return membersCount;
  }

  public Integer getTargetMembersCount() {
    return targetMembersCount;
  }

  public Integer getCost() {
    return cost;
  }
}
