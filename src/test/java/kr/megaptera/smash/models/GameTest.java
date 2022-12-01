package kr.megaptera.smash.models;

import kr.megaptera.smash.exceptions.AlreadyJoinedGame;
import kr.megaptera.smash.exceptions.GameIsFull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {
    @Test
    void countCurrentMembers() {
        Game game = Game.fake("운동 이름", "장소 이름");
        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.accepted()),
            Register.fake(2L, game.id(), RegisterStatus.accepted()),
            Register.fake(3L, game.id(), RegisterStatus.accepted()),
            Register.fake(4L, game.id(), RegisterStatus.processing()),
            Register.fake(5L, game.id(), RegisterStatus.canceled()),
            Register.fake(6L, game.id(), RegisterStatus.rejected())
        );

        assertThat(game.countCurrentMembers(registers)).isEqualTo(3);
    }

    @Test
    void findMyRegisterWithProcessing() {
        Game game = Game.fake("운동 이름", "장소 이름");
        User user = User.fake(5L);
        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.canceled()),
            Register.fake(5L, game.id(), RegisterStatus.processing())
        );

        Register register = game.findMyRegister(user, registers);
        assertThat(register.userId()).isEqualTo(5L);
        assertThat(register.status()).isEqualTo(RegisterStatus.processing());
    }

    @Test
    void findMyRegisterWithAccepted() {
        Game game = Game.fake("운동 이름", "장소 이름");
        User user = User.fake(1L);
        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.accepted()),
            Register.fake(5L, game.id(), RegisterStatus.rejected())
        );

        Register register = game.findMyRegister(user, registers);
        assertThat(register.userId()).isEqualTo(1L);
        assertThat(register.status()).isEqualTo(RegisterStatus.accepted());
    }

    @Test
    void cannotFoundMyRegister() {
        Game game = Game.fake("운동 이름", "장소 이름");
        User user = User.fake(2L);
        List<Register> registers = List.of(
            Register.fake(3L, game.id(), RegisterStatus.accepted()),
            Register.fake(4L, game.id(), RegisterStatus.processing())
        );

        Register register = game.findMyRegister(user, registers);
        assertThat(register).isNull();
    }

    @Test
    void cannotFoundMyRegisterWhenUserIsNull() {
        Game game = Game.fake("운동 이름", "장소 이름");
        User user = null;
        List<Register> registers = List.of(
            Register.fake(3L, game.id(), RegisterStatus.accepted())
        );

        Register register = game.findMyRegister(user, registers);
        assertThat(register).isNull();
    }

    @Test
    void join() {
        Game game = Game.fake("운동 이름", "장소 이름");
        User user = User.fake(4L);
        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.accepted()),
            Register.fake(2L, game.id(), RegisterStatus.accepted()),
            Register.fake(3L, game.id(), RegisterStatus.accepted())
        );

        Register register = game.join(user, registers);
        assertThat(register.status()).isEqualTo(RegisterStatus.processing());
    }

    @Test
    void alreadyJoined() {
        Game game = Game.fake("운동 이름", "장소 이름");
        User user = User.fake(1L);
        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.processing()),
            Register.fake(2L, game.id(), RegisterStatus.accepted()),
            Register.fake(3L, game.id(), RegisterStatus.accepted())
        );

        assertThrows(AlreadyJoinedGame.class, () -> game.join(user, registers));
    }

    @Test
    void gameIsFull() {
        Game game = Game.fake(new GameTargetMemberCount(2));
        User user = User.fake(5L);
        List<Register> registers = List.of(
            Register.fake(1L, game.id(), RegisterStatus.accepted()),
            Register.fake(2L, game.id(), RegisterStatus.accepted()),
            Register.fake(3L, game.id(), RegisterStatus.rejected()),
            Register.fake(4L, game.id(), RegisterStatus.processing())
        );

        assertThrows(GameIsFull.class, () -> game.join(user, registers));
    }

    @Test
    void createGameDateTime() {
        Game game = Game.fake(1L);

        String gameDate = "2022-4-3T00:00:00.000Z";
        String gameStartTimeAmPm = "am";
        String gameStartHour = "07";
        String gameStartMinute = "30";
        String gameEndTimeAmPm = "pm";
        String gameEndHour = "11";
        String gameEndMinute = "00";

        game.createGameDateTime(
            gameDate,
            gameStartTimeAmPm,
            gameStartHour,
            gameStartMinute,
            gameEndTimeAmPm,
            gameEndHour,
            gameEndMinute
        );

        assertThat(game.dateTime().date())
            .isEqualTo(LocalDate.of(2022, 4, 3));
        assertThat(game.dateTime().startTime())
            .isEqualTo(LocalTime.of(7, 30));
        assertThat(game.dateTime().endTime())
            .isEqualTo(LocalTime.of(23, 0));
    }
}
