package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameDetailDto;
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

    private Long placeId;

    @Embedded
    private Exercise exercise;

    @Embedded
    private GameDateTime dateTime;

    @Embedded
    private GameTargetMemberCount targetMemberCount;

    @Transient
    private List<Register> registers;

    private Game() {

    }

    public Game(Long postId,
                Long placeId,
                Exercise exercise,
                GameTargetMemberCount targetMemberCount
    ) {
        this.postId = postId;
        this.placeId = placeId;
        this.exercise = exercise;
        this.targetMemberCount = targetMemberCount;
    }

    public Game(Long postId,
                Long placeId,
                Exercise exercise,
                GameDateTime dateTime,
                GameTargetMemberCount targetMemberCount
    ) {
        this.postId = postId;
        this.placeId = placeId;
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.targetMemberCount = targetMemberCount;
    }

    public Game(Long id,
                Long postId,
                Long placeId,
                Exercise exercise,
                GameDateTime dateTime,
                GameTargetMemberCount targetMemberCount
    ) {
        this.id = id;
        this.postId = postId;
        this.placeId = placeId;
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.targetMemberCount = targetMemberCount;
    }

    public Long id() {
        return id;
    }

    public Long postId() {
        return postId;
    }

    public Long placeId() {
        return placeId;
    }

    public Exercise exercise() {
        return exercise;
    }

    public GameDateTime dateTime() {
        return dateTime;
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
        register.accept();
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

    public boolean isFull(List<Register> registers) {
        this.registers = registers;

        long count = registers.stream()
            .filter(Register::accepted)
            .count();
        return targetMemberCount.reach(count);
    }

    public static Game fake(Long gameId) {
        Long postId = 1L;
        Long placeId = 1L;
        return new Game(
            gameId,
            postId,
            placeId,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new GameTargetMemberCount(10)
        );
    }

    public static Game fake(String exerciseName,
                            Long placeId) {
        Long gameId = 1L;
        Long postId = 1L;
        return new Game(
            gameId,
            postId,
            placeId,
            new Exercise(exerciseName),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new GameTargetMemberCount(10)
        );
    }

    public static Game fake(String exerciseName,
                            GameDateTime gameDateTime) {
        Long gameId = 1L;
        Long postId = 1L;
        Long placeId = 1L;
        return new Game(
            gameId,
            postId,
            placeId,
            new Exercise(exerciseName),
            gameDateTime,
            new GameTargetMemberCount(10)
        );
    }

    public static Game fake(GameTargetMemberCount targetMemberCount) {
        Long gameId = 1L;
        Long postId = 1L;
        Long placeId = 1L;
        return new Game(
            gameId,
            postId,
            placeId,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new GameTargetMemberCount(targetMemberCount.value())
        );
    }

    public static Game fake(Long postId,
                            GameTargetMemberCount targetMemberCount) {
        Long gameId = 1L;
        Long placeId = 1L;
        return new Game(
            gameId,
            postId,
            placeId,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new GameTargetMemberCount(targetMemberCount.value())
        );
    }

    public static Game fake(Long gameId,
                            Long postId,
                            GameTargetMemberCount targetMemberCount) {
        Long placeId = 1L;
        return new Game(
            gameId,
            postId,
            placeId,
            new Exercise("운동 이름"),
            new GameDateTime(
                LocalDate.of(2022, 12, 24),
                LocalTime.of(10, 0),
                LocalTime.of(16, 30)
            ),
            new GameTargetMemberCount(targetMemberCount.value())
        );
    }

    public static List<Game> fakes(long generationCount,
                                   GameTargetMemberCount gameTargetMemberCount) {
        List<Game> games = new ArrayList<>();
        for (long i = 1; i <= generationCount; i += 1) {
            Long gameId = i;
            Long postId = i;
            Long placeId = i;
            Game game = new Game(
                gameId,
                postId,
                placeId,
                new Exercise("운동 종류 " + i),
                new GameDateTime(
                    LocalDate.of(2022, 12, 24),
                    LocalTime.of(10, 0),
                    LocalTime.of(16, 30)
                ),
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
            currentMemberCount,
            targetMemberCount.value(),
            registerId,
            registerStatus
        );
    }

    public GameDetailDto toGameDetailDto(Integer currentMemberCount,
                                         Long registerId,
                                         String registerStatus) {
        return new GameDetailDto(
            id,
            placeId,
            exercise.name(),
            dateTime.joinDateAndTime(),
            currentMemberCount,
            targetMemberCount.value(),
            registerId,
            registerStatus
        );
    }
}
