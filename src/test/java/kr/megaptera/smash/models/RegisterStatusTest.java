package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterStatusTest {
    @Test
    void equals() {
        assertThat(RegisterStatus.processing())
            .isEqualTo(RegisterStatus.processing());
        assertThat(RegisterStatus.canceled())
            .isEqualTo(RegisterStatus.canceled());
        assertThat(RegisterStatus.accepted())
            .isEqualTo(RegisterStatus.accepted());
        assertThat(RegisterStatus.canceled())
            .isEqualTo(RegisterStatus.canceled());
    }
}
