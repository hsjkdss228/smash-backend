package kr.megaptera.smash.models.game;

import kr.megaptera.smash.models.game.GameDateTime;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class GameDateTimeTest {
    @Test
    void joinDateAndTime() {
        LocalDate date = LocalDate.of(2022, 12, 22);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 55);
        GameDateTime gameDateTime = new GameDateTime(
            date,
            startTime,
            endTime
        );

        String dateAndTime = gameDateTime.joinDateAndTime();
        assertThat(dateAndTime)
            .isEqualTo("2022년 12월 22일 오전 09:00 ~ 오전 11:55");
    }

    @Test
    void joinDateAndTimeConvertWithMidnightAndNoon() {
        LocalDate date = LocalDate.of(2022, 12, 22);
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        GameDateTime gameDateTime = new GameDateTime(
            date,
            startTime,
            endTime
        );

        String dateAndTime = gameDateTime.joinDateAndTime();
        assertThat(dateAndTime)
            .isEqualTo("2022년 12월 22일 오전 12:00 ~ 오후 12:00");
    }

    @Test
    void joinDateAndTimeConvertWithPm() {
        LocalDate date = LocalDate.of(2022, 12, 22);
        LocalTime startTime = LocalTime.of(15, 30);
        LocalTime endTime = LocalTime.of(21, 0);
        GameDateTime gameDateTime = new GameDateTime(
            date,
            startTime,
            endTime
        );

        String dateAndTime = gameDateTime.joinDateAndTime();
        assertThat(dateAndTime)
            .isEqualTo("2022년 12월 22일 오후 03:30 ~ 오후 09:00");
    }
}
