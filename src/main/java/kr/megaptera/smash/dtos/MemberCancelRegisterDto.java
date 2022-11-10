package kr.megaptera.smash.dtos;

public class MemberCancelRegisterDto {
  private Long roleId;

  public MemberCancelRegisterDto() {

  }

  public MemberCancelRegisterDto(Long roleId) {
    this.roleId = roleId;
  }

  public Long getRoleId() {
    return roleId;
  }
}
