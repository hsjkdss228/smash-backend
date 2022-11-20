package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Game> fakes(long generationCount) {
        List<Game> games = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Game game = new Game(
                id,
                id,
                new Exercise("운동 종류"),
                new GameDate("운동 날짜"),
                new Place("운동 장소"),
                new GameTargetMemberCount(10)
            );
            games.add(game);
        }
        return games;
    }

    public static Game fake(String exerciseName, String placeName) {
        return new Game(
            1L,
            1L,
            new Exercise(exerciseName),
            new GameDate("경기 날짜"),
            new Place(placeName),
            new GameTargetMemberCount(10)
        );
    }

    public GameInPostListDto toGameInPostListDto(Integer currentMemberCount,
                                                 Long registerId,
                                                 String registerStatus) {
        return new GameInPostListDto(
            id,
            exercise.name(),
            date.value(),
            place.name(),
            currentMemberCount,
            targetMemberCount.value(),
            registerId,
            registerStatus
        );
    }
}
