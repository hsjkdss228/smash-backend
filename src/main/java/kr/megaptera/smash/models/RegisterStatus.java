package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class RegisterStatus {
    public static final String PROCESSING = "processing";
    public static final String CANCELED = "canceled";
    public static final String ACCEPTED = "accepted";
    public static final String REJECTED = "rejected";

    @Column(name = "status")
    private String value;

    private RegisterStatus() {

    }

    public RegisterStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public void changeToCanceled() {
        this.value = RegisterStatus.CANCELED;
    }

    public void changeToAccepted() {
        this.value = RegisterStatus.ACCEPTED;
    }

    public void changeToRejected() {
        this.value = RegisterStatus.REJECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterStatus that = (RegisterStatus) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "RegisterStatus{" +
            "value='" + value + '\'' +
            '}';
    }
}
