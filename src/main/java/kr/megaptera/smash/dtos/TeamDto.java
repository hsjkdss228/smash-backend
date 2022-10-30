package kr.megaptera.smash.dtos;

public class TeamDto {
  private final Long id;

  private final Long postId;

  private final String name;

  private final Integer membersCount;

  private final Integer targetMembersCount;

  public TeamDto(Long id,
                 Long postId,
                 String name,
                 Integer membersCount,
                 Integer targetMembersCount) {
    this.id = id;
    this.postId = postId;
    this.name = name;
    this.membersCount = membersCount;
    this.targetMembersCount = targetMembersCount;
  }

  public Long getId() {
    return id;
  }

  public Long getPostId() {
    return postId;
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
}
