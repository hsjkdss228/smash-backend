package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.MemberDto;
import kr.megaptera.smash.dtos.RoleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Role {
  @Id
  @GeneratedValue
  private Long id;

  private Long gameId;

  private Long teamId;

  private String name;

  private Integer targetParticipantsCount;

  public Role() {

  }

  public Role(Long id,
              Long gameId,
              Long teamId,
              String name,
              Integer targetParticipantsCount) {
    this.id = id;
    this.gameId = gameId;
    this.teamId = teamId;
    this.name = name;
    this.targetParticipantsCount = targetParticipantsCount;
  }

  public Long id() {
    return id;
  }

  public Long gameId() {
    return gameId;
  }

  public Long teamId() {
    return teamId;
  }

  public String name() {
    return name;
  }

  public Integer targetParticipantsCount() {
    return targetParticipantsCount;
  }

  public RoleDto toRoleDto(Integer participantsCount,
                       List<MemberDto> members
  ) {
    return new RoleDto(
        id,
        teamId,
        name,
        participantsCount,
        targetParticipantsCount,
        members
    );
  }
}
