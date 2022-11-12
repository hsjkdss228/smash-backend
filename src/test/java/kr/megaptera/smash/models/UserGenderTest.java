package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserGenderTest {
    @Test
    void equals() {
        assertThat(new UserGender("남성")).isEqualTo(new UserGender("남성"));
        assertThat(new UserGender("남성")).isNotEqualTo(new UserGender("여성"));
        assertThat(new UserGender("남성")).isNotEqualTo(null);
        assertThat(new UserGender("남성")).isNotEqualTo("남성");
    }
}
