package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserNameTest {
    @Test
    void equals() {
        assertThat(new UserName("황인우")).isEqualTo(new UserName("황인우"));
        assertThat(new UserName("황인우")).isNotEqualTo(new UserName("김인우"));
        assertThat(new UserName("황인우")).isNotEqualTo(null);
        assertThat(new UserName("황인우")).isNotEqualTo("황인우");
    }
}
