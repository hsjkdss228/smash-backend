package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.RoleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Role {
  @Id
  @GeneratedValue
  private Long id;

  private Long teamId;

  private String name;

  private Integer targetParticipantsCount;

  public Role() {

  }

  public Role(Long id,
              Long teamId,
              String name,
              Integer targetParticipantsCount) {
    this.id = id;
    this.teamId = teamId;
    this.name = name;
    this.targetParticipantsCount = targetParticipantsCount;
  }

  public Long id() {
    return id;
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

  public RoleDto toDto(Integer participantsCount) {
    return new RoleDto(
        id,
        teamId,
        name,
        participantsCount,
        targetParticipantsCount
    );
  }
}
