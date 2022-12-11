package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileImageTest {
    @Test
    void defaultImage() {
        assertThat(UserProfileImage.defaultImage())
            .isEqualTo(new UserProfileImage(
                "https://user-images.githubusercontent.com/" +
                    "50052512/206894378-535d28d7-6164-4b54-91ef-200cc515f4fb.png"));
    }
}
