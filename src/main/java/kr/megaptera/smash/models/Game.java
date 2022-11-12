package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "games")
public class Game {
  @Id
  @GeneratedValue
  private Long id;

  private Long postId;

  @Embedded
  private Exercise exercise;

  @Embedded
  private GameDate date;

  @Embedded
  private Place place;

  @Embedded
  private GameTargetMemberCount targetMemberCount;

  private Game() {

  }

  public Game(Long id,
              Long postId,
              Exercise exercise,
              GameDate date,
              Place place,
              GameTargetMemberCount targetMemberCount
  ) {
    this.id = id;
    this.postId = postId;
    this.exercise = exercise;
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

  public Exercise exercise() {
    return exercise;
  }

  public GameDate date() {
    return date;
  }

  public Place place() {
    return place;
  }

  public GameTargetMemberCount targetMemberCount() {
    return targetMemberCount;
  }

  public static Game fake(Long id, Long postId) {
    return new Game(
        id,
        postId,
        new Exercise("운동 종류"),
        new GameDate("운동 날짜"),
        new Place("운동 장소"),
        new GameTargetMemberCount(5)
    );
  }

  public GameInPostListDto toGameInPostListDto(Integer currentMemberCount,
                                               Boolean isRegistered) {
    return new GameInPostListDto(
        id,
        exercise.name(),
        date.value(),
        place.name(),
        currentMemberCount,
        targetMemberCount.value(),
        isRegistered
    );
  }
}
