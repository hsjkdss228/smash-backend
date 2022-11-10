package kr.megaptera.smash.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  private Long gameId;

  private String name;

  private Member() {

  }

  public Member(Long id, Long gameId, String name) {
    this.id = id;
    this.gameId = gameId;
    this.name = name;
  }

  // TODO: 알아볼 수 없는 값들을 값 객체로 정의

  public static Member fake(Long id, Long gameId) {
    return new Member(
      id,
      gameId,
      "참가자 이름"
    );
  }

  public Long gameId() {
    return gameId;
  }
}
