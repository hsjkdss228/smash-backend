package kr.megaptera.smash.models.user;

import kr.megaptera.smash.models.user.UserPersonalInformation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersonalInformationTest {
    @Test
    void equals() {
        UserPersonalInformation personalInformation
            = new UserPersonalInformation(
            "사용자 이름",
            "성별",
            "01012345678"
        );
        assertThat(personalInformation).isEqualTo(
            new UserPersonalInformation(
                "사용자 이름",
                "성별",
                "01012345678"
            )
        );
    }

    @Test
    void notEquals() {
        UserPersonalInformation personalInformation
            = new UserPersonalInformation(
            "사용자 이름",
            "남성",
            "01012345678"
        );
        assertThat(personalInformation).isNotEqualTo(
            new UserPersonalInformation(
                "2름",
                "남성",
                "01012345678"
            )
        );
        assertThat(personalInformation).isNotEqualTo(
            new UserPersonalInformation(
                "사용자 이름",
                "여성",
                "01012345678"
            )
        );
        assertThat(personalInformation).isNotEqualTo(
            new UserPersonalInformation(
                "사용자 이름",
                "남성",
                "01000000000"
            )
        );
    }

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
