package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.MemberDto;
import kr.megaptera.smash.dtos.MemberRegisterResultDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  private Long gameId;

  private Long teamId;

  private Long roleId;

  private Long userId;

  public Member() {

  }

  public Member(Long gameId,
                Long teamId,
                Long roleId,
                Long userId) {
    this.gameId = gameId;
    this.teamId = teamId;
    this.roleId = roleId;
    this.userId = userId;
  }

  public Member(Long id,
                Long gameId,
                Long teamId,
                Long roleId,
                Long userId) {
    this.id = id;
    this.gameId = gameId;
    this.teamId = teamId;
    this.roleId = roleId;
    this.userId = userId;
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

  public Long roleId() {
    return roleId;
  }

  public Long userId() {
    return userId;
  }

  public MemberDto toMemberDto(String name,
                         Double mannerScore) {
    return new MemberDto(
        id,
        teamId,
        roleId,
        name,
        mannerScore
    );
  }

  public MemberRegisterResultDto toRegisterResultDto() {
    return new MemberRegisterResultDto(
        id
    );
  }
}
