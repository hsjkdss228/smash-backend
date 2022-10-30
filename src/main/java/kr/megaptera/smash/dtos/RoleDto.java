package kr.megaptera.smash.dtos;

public class RoleDto {
  private final Long id;

  private final Long teamId;

  private final String name;

  private final Integer currentParticipants;

  private final Integer targetParticipantsCount;

  public RoleDto(Long id,
                 Long teamId,
                 String name,
                 Integer currentParticipants,
                 Integer targetParticipantsCount) {
    this.id = id;
    this.teamId = teamId;
    this.name = name;
    this.currentParticipants = currentParticipants;
    this.targetParticipantsCount = targetParticipantsCount;
  }

  public Long getId() {
    return id;
  }

  public Long getTeamId() {
    return teamId;
  }

  public String getName() {
    return name;
  }

  public Integer getCurrentParticipants() {
    return currentParticipants;
  }

  public Integer getTargetParticipantsCount() {
    return targetParticipantsCount;
  }
}
