package kr.megaptera.smash.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  private Long userId;

  private Long gameId;

  private String name;

  private Member() {

  }

  public Member(Long id,
                Long userId,
                Long gameId,
                String name) {
    this.id = id;
    this.userId = userId;
    this.gameId = gameId;
    this.name = name;
  }

  // TODO: 알아볼 수 없는 값들을 값 객체로 정의

  public static Member fake(Long id,
                            Long userId,
                            Long gameId) {
    return new Member(
      id,
      userId,
      gameId,
      "참가자 이름"
    );
  }

  public Long userId() {
    return userId;
  }

  public Long gameId() {
    return gameId;
  }
}
