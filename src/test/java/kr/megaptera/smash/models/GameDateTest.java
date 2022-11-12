package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameDateTest {
    @Test
    void equals() {
        assertThat(new GameDate("2022년 12월 22일 12:00~15:00"))
            .isEqualTo(new GameDate("2022년 12월 22일 12:00~15:00"));
        assertThat(new GameDate("2022년 12월 22일 12:00~15:00"))
            .isNotEqualTo(new GameDate("1995년 1월 2일 01:00~04:00"));
        assertThat(new GameDate("2022년 12월 22일 12:00~15:00"))
            .isNotEqualTo(null);
        assertThat(new GameDate("2022년 12월 22일 12:00~15:00"))
            .isNotEqualTo("2022년 12월 22일 12:00~15:00");
    }
}
