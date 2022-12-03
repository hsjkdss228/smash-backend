package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.exceptions.AlreadyJoinedGame;
import kr.megaptera.smash.exceptions.GameIsFull;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private GameDateTime dateTime;

    @Embedded
    private Place place;

    @Embedded
    private GameTargetMemberCount targetMemberCount;

    @Transient
    private List<Register> registers;

    private Game() {

    }

    public Game(Long postId,
                Exercise exercise,
                Place place,
                GameTargetMemberCount targetMemberCount
    ) {
        this.postId = postId;
        this.exercise = exercise;
        this.place = place;
        this.targetMemberCount = targetMemberCount;
    }

    public Game(Long postId,
                Exercise exercise,
                GameDateTime dateTime,
                Place place,
                GameTargetMemberCount targetMemberCount
    ) {
        this.postId = postId;
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.place = place;
        this.targetMemberCount = targetMemberCount;
    }

    public Game(Long id,
                Long postId,
                Exercise exercise,
                GameDateTime dateTime,
                Place place,
                GameTargetMemberCount targetMemberCount
    ) {
        this.id = id;
        this.postId = postId;
        this.exercise = exercise;
        this.dateTime = dateTime;
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

    public GameDateTime dateTime() {
        return dateTime;
    }

    public Place place() {
        return place;
    }

    public GameTargetMemberCount targetMemberCount() {
        return targetMemberCount;
    }

    public void createGameDateTime(String gameDate,
                                   String gameStartTimeAmPm,
                                   String gameStartHour,
                                   String gameStartMinute,
                                   String gameEndTimeAmPm,
                                   String gameEndHour,
                                   String gameEndMinute
    ) {
        String[] yearMonthDay = gameDate.split("-");
        int year = parseDateType(yearMonthDay[0]);
        int month = parseDateType(yearMonthDay[1]);
        int day = parseDateType(yearMonthDay[2].split("T")[0]);

        int startHour = calculateHour(
            Integer.parseInt(gameStartHour),
            gameStartTimeAmPm
        );
        int endHour = calculateHour(
            Integer.parseInt(gameEndHour),
            gameEndTimeAmPm
        );
        int startMinute = Integer.parseInt(gameStartMinute);
        int endMinute = Integer.parseInt(gameEndMinute);

        this.dateTime = new GameDateTime(
            LocalDate.of(year, month, day),
            LocalTime.of(startHour, startMinute),
            LocalTime.of(endHour, endMinute)
        );
    }

    private int parseDateType(String dateType) {
        return Integer.parseInt(dateType);
    }

    private int calculateHour(int hour,
                              String timeAmPm) {
        if (hour == 12 && timeAmPm.equals("am")) {
            return 0;
        }
        if (hour < 12 && timeAmPm.equals("pm")) {
            return hour + 12;
        }
        return hour;
    }

    public Integer countCurrentMembers(List<Register> registers) {
        return (int) registers.stream()
            .filter(Register::accepted)
            .count();
    }

    public Register findMyRegister(User currentUser, List<Register> registers) {
        if (currentUser == null) {
            return null;
        }

        return registers.stream()
            .filter(register -> register.match(currentUser))
            .filter(Register::active)
            .findFirst().orElse(null);
    }

    public Register join(User currentUser, List<Register> registers) {
        this.registers = registers;

        if (alreadyJoined(currentUser)) {
            throw new AlreadyJoinedGame(id);
        }

        if (isFull()) {
            throw new GameIsFull(id);
        }

        return new Register(currentUser, this);
    }

    public Register join(User postAuthor) {
        Register register = new Register(postAuthor, this);
        register.acceptRegister();
        return register;
    }

    private boolean alreadyJoined(User user) {
        return registers.stream()
            .filter(Register::active)
            .anyMatch(register -> register.match(user));
    }

    private boolean isFull() {
        long count = registers.stream()
            .filter(Register::accepted)
            .count();
        return targetMemberCount.reach(count);
    }

    public static Game fake(Long gameId) {
        return new Game(
            gameId,
            1L,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new Place("운동 장소"),
            new GameTargetMemberCount(10)
        );
    }

    public static Game fake(String exerciseName, String placeName) {
        return new Game(
            1L,
            1L,
            new Exercise(exerciseName),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new Place(placeName),
            new GameTargetMemberCount(10)
        );
    }

    public static Game fake(String exerciseName,
                            GameDateTime gameDateTime) {
        return new Game(
            1L,
            1L,
            new Exercise(exerciseName),
            gameDateTime,
            new Place("운동 장소"),
            new GameTargetMemberCount(10)
        );
    }

    public static Game fake(GameTargetMemberCount targetMemberCount) {
        return new Game(
            1L,
            1L,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new Place("운동 장소"),
            new GameTargetMemberCount(targetMemberCount.value())
        );
    }

    public static Game fake(Long postId,
                            GameTargetMemberCount targetMemberCount) {
        return new Game(
            1L,
            postId,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new Place("운동 장소"),
            new GameTargetMemberCount(targetMemberCount.value())
        );
    }

    public static List<Game> fakes(long generationCount,
                                   GameTargetMemberCount gameTargetMemberCount) {
        List<Game> games = new ArrayList<>();
        for (long i = 1; i <= generationCount; i += 1) {
            Long gameId = i;
            Long postId = i;
            Game game = new Game(
                gameId,
                postId,
                new Exercise("운동 종류 " + i),
                new GameDateTime(
                    LocalDate.of(2022, 12, 24),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 30)
                ),
                new Place("운동 장소 " + i),
                gameTargetMemberCount
            );
            games.add(game);
        }
        return games;
    }

    public GameInPostListDto toGameInPostListDto(Integer currentMemberCount,
                                                 Long registerId,
                                                 String registerStatus) {
        return new GameInPostListDto(
            id,
            exercise.name(),
            dateTime.joinDateAndTime(),
            place.name(),
            currentMemberCount,
            targetMemberCount.value(),
            registerId,
            registerStatus
        );
    }
}
