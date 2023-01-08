package kr.megaptera.smash.models.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserProfileImage {
    @Column(name = "profile_image_url", length = 2048)
    private String url;

    private UserProfileImage() {

    }

    public UserProfileImage(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    public static UserProfileImage defaultImage() {
        return new UserProfileImage(
            "https://user-images.githubusercontent.com/" +
                "50052512/206894378-535d28d7-6164-4b54-91ef-200cc515f4fb.png"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserProfileImage that = (UserProfileImage) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return url;
    }
}
