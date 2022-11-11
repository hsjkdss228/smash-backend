package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Game {
  @Id
  @GeneratedValue
  private Long id;

  private Long postId;

  private String type;

  private String date;

  private String place;

  private Integer targetMemberCount;

  private Game() {

  }

  public Game(Long id,
              Long postId,
              String type,
              String date,
              String place,
              Integer targetMemberCount
  ) {
    this.id = id;
    this.postId = postId;
    this.type = type;
    this.date = date;
    this.place = place;
    this.targetMemberCount = targetMemberCount;
  }

  public Long id() {
    return id;
  }

  public Long postId() {
    return postId;
  }

  public String type() {
    return type;
  }

  public String date() {
    return date;
  }

  public String place() {
    return place;
  }

  public Integer targetMemberCount() {
    return targetMemberCount;
  }

  // TODO: 알아볼 수 없는 값들을 값 객체로 정의

  public static Game fake(Long id, Long postId) {
    return new Game(
        id,
        postId,
        "운동 종류",
        "운동 날짜",
        "운동 장소",
        5
    );
  }

  public GameInPostListDto toGameInPostListDto(Integer currentMemberCount,
                                               Boolean isRegistered) {
    return new GameInPostListDto(
        id, type,
        date,
        place,
        currentMemberCount,
        targetMemberCount,
        isRegistered
    );
  }
}
