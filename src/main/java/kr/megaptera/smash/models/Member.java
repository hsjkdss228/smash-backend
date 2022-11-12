package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "members")
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  private Long userId;

  private Long gameId;

  @Embedded
  private MemberName name;

  private Member() {

  }

  public Member(Long id,
                Long userId,
                Long gameId,
                MemberName name
  ) {
    this.id = id;
    this.userId = userId;
    this.gameId = gameId;
    this.name = name;
  }

  public Member(Long userId,
                Long gameId,
                MemberName name
  ) {
    this.userId = userId;
    this.gameId = gameId;
    this.name = name;
  }

  public Long userId() {
    return userId;
  }

  public Long gameId() {
    return gameId;
  }

  public static Member fake(Long id,
                            Long userId,
                            Long gameId
  ) {
    return new Member(
      id,
      userId,
      gameId,
      new MemberName("참가자 이름")
    );
  }

  public static Member fake(Long id,
                            Long userId,
                            Long gameId,
                            MemberName name
  ) {
    return new Member(
        id,
        userId,
        gameId,
        name
    );
  }
}
