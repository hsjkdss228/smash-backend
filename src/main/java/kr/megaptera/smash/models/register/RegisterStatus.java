package kr.megaptera.smash.models.register;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

// TODO: enum 타입으로 리팩터링

@Embeddable
public class RegisterStatus {
    private static final RegisterStatus PROCESSING
        = new RegisterStatus("processing");
    private static final RegisterStatus CANCELED
        = new RegisterStatus("canceled");
    private static final RegisterStatus ACCEPTED
        = new RegisterStatus("accepted");
    private static final RegisterStatus REJECTED
        = new RegisterStatus("rejected");

    @Column(name = "status")
    private String value;

    private RegisterStatus() {

    }

    public RegisterStatus(String value) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterStatus that = (RegisterStatus) o;
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
