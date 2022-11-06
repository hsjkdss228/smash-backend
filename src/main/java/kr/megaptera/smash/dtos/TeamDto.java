package kr.megaptera.smash.dtos;

import java.util.List;

public class TeamDto {
  private final Long id;

  private final Long gameId;

  private final String name;

  private final Integer membersCount;

  private final Integer targetMembersCount;

  private final List<RoleDto> roles;

  public TeamDto(Long id,
                 Long gameId,
                 String name,
                 Integer membersCount,
                 Integer targetMembersCount,
                 List<RoleDto> roles
  ) {
    this.id = id;
    this.gameId = gameId;
    this.name = name;
    this.membersCount = membersCount;
    this.targetMembersCount = targetMembersCount;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public Long getGameId() {
    return gameId;
  }

  public String getName() {
    return name;
  }

  public Integer getMembersCount() {
    return membersCount;
  }

  public Integer getTargetMembersCount() {
    return targetMembersCount;
  }

  public List<RoleDto> getRoles() {
    return roles;
  }
}
