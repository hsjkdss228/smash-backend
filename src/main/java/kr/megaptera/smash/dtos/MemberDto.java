package kr.megaptera.smash.dtos;

public class MemberDto {
  private final Long id;

  private final Long teamId;

  private final Long roleId;

  private final String name;

  private final Double mannerScore;


  public MemberDto(Long id,
                   Long teamId,
                   Long roleId,
                   String name,
                   Double mannerScore) {
    this.id = id;
    this.teamId = teamId;
    this.roleId = roleId;
    this.name = name;
    this.mannerScore = mannerScore;
  }

  public Long getId() {
    return id;
  }

  public Long getTeamId() {
    return teamId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public String getName() {
    return name;
  }

  public Double getMannerScore() {
    return mannerScore;
  }
}
