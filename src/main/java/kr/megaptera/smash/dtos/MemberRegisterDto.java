package kr.megaptera.smash.dtos;

public class MemberRegisterDto {
  private final Long gameId;

  private final Long teamId;

  private final Long roleId;

  public MemberRegisterDto(Long gameId, Long teamId, Long roleId) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.roleId = roleId;
  }

  public Long getGameId() {
    return gameId;
  }

  public Long getTeamId() {
    return teamId;
  }

  public Long getRoleId() {
    return roleId;
  }
}
