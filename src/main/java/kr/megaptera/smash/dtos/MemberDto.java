package kr.megaptera.smash.dtos;

public class MemberDto {
  private final Long id;

  private final String name;

  private final Long teamId;

  private final Long positionId;

  private final Double mannerScore;


  public MemberDto(Long id,
                   String name,
                   Long teamId,
                   Long roleId,
                   Double mannerScore) {
    this.id = id;
    this.name = name;
    this.teamId = teamId;
    this.positionId = roleId;
    this.mannerScore = mannerScore;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getTeamId() {
    return teamId;
  }

  public Long getPositionId() {
    return positionId;
  }

  public Double getMannerScore() {
    return mannerScore;
  }
}
