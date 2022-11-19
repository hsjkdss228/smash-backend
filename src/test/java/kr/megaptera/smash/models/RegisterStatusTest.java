package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterStatusTest {
    @Test
    void changeToCanceledWhenAccepted() {
        RegisterStatus status = new RegisterStatus(RegisterStatus.ACCEPTED);
        status.changeToCanceled();
        assertThat(status.value()).isEqualTo(RegisterStatus.CANCELED);
    }

    @Test
    void changeToCanceledWhenProcessing() {
        RegisterStatus status = new RegisterStatus(RegisterStatus.PROCESSING);
        status.changeToCanceled();
        assertThat(status.value()).isEqualTo(RegisterStatus.CANCELED);
    }

    @Test
    void changeToAccepted() {
        RegisterStatus status = new RegisterStatus(RegisterStatus.PROCESSING);
        status.changeToAccepted();
        assertThat(status.value()).isEqualTo(RegisterStatus.ACCEPTED);
    }

    @Test
    void changeToRejected() {
        RegisterStatus status = new RegisterStatus(RegisterStatus.PROCESSING);
        status.changeToRejected();
        assertThat(status.value()).isEqualTo(RegisterStatus.REJECTED);
    }
}
