package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.MemberDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  private Long teamId;

  private Long roleId;

  private Long userId;

  public Member() {

  }

  public Member(Long id,
                Long teamId,
                Long roleId,
                Long userId) {
    this.id = id;
    this.teamId = teamId;
    this.roleId = roleId;
    this.userId = userId;
  }

  public Long id() {
    return id;
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

  public MemberDto toDto(String name,
                         Double mannerScore) {
    return new MemberDto(
        id,
        name,
        teamId,
        roleId,
        mannerScore
    );
  }
}
