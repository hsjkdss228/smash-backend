package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserPersonalInformation {
    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    private UserPersonalInformation() {

    }

    public UserPersonalInformation(String name,
                                   String gender,
                                   String phoneNumber
    ) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = formatPhoneNumber(phoneNumber);
    }

    public String name() {
        return name;
    }

    public String gender() {
        return gender;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public String formatPhoneNumber(String phoneNumber) {
        return phoneNumber.substring(0, 3) + "-"
            + phoneNumber.substring(3, 7) + "-"
            + phoneNumber.substring(7);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPersonalInformation that = (UserPersonalInformation) o;
        return Objects.equals(name, that.name)
            && Objects.equals(gender, that.gender)
            && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, phoneNumber);
    }

    @Override
    public String toString() {
        return "UserPersonalInformation{" +
            "name='" + name + '\'' +
            ", gender='" + gender + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}
