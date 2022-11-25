package kr.megaptera.smash.models;

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
            .isEqualTo("2022년 12월 22일 09:00~11:55");
    }
}
