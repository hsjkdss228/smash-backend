package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersonalInformationTest {
    @Test
    void formatPhoneNumber() {
        UserPersonalInformation personalInformation
            = new UserPersonalInformation(
            "사용자 이름",
            "성별",
            "01012345678"
        );
        assertThat(personalInformation.phoneNumber()).isEqualTo("010-1234-5678");
    }
}
