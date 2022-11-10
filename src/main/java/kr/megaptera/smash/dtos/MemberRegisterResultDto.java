package kr.megaptera.smash.dtos;

public class MemberRegisterResultDto {
  private final Long id;

  public MemberRegisterResultDto(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
