package kr.megaptera.smash.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RegisterStatus {
    private static final RegisterStatus PROCESSING =
            new RegisterStatus("processing");
    private static final RegisterStatus CANCELED =
            new RegisterStatus("canceled");
    private static final RegisterStatus ACCEPTED =
            new RegisterStatus("accepted");
    private static final RegisterStatus REJECTED =
            new RegisterStatus("rejected");

    @Column(name = "status")
    private String value;

    private RegisterStatus() {

    }

    private RegisterStatus(String value) {
        this.value = value;
    }

    public static RegisterStatus processing() {
        return PROCESSING;
    }

    public static RegisterStatus canceled() {
        return CANCELED;
    }

    public static RegisterStatus accepted() {
        return ACCEPTED;
    }

    public static RegisterStatus rejected() {
        return REJECTED;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        RegisterStatus that = (RegisterStatus) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
