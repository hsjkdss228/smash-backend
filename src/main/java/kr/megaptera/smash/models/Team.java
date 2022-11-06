package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.RoleDto;
import kr.megaptera.smash.dtos.TeamDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Team {
  @Id
  @GeneratedValue
  private Long id;

  private Long gameId;

  private String name;

  private Integer targetMembersCount;

  public Team() {

  }

  public Team(Long id,
              Long gameId,
              String name,
              Integer targetMembersCount
  ) {
    this.id = id;
    this.gameId = gameId;
    this.name = name;
    this.targetMembersCount = targetMembersCount;
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

  public Integer targetMembersCount() {
    return targetMembersCount;
  }

  public TeamDto toTeamDto(Integer membersCount, List<RoleDto> roles) {
    return new TeamDto(
        id,
        gameId,
        name,
        membersCount,
        targetMembersCount,
        roles
    );
  }
}
