package kr.megaptera.smash.models;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "registers")
public class Register {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long gameId;

    @Embedded
    private RegisterStatus status;

    private Register() {

    }

    public Register(Long id,
                    Long userId,
                    Long gameId,
                    RegisterStatus status
    ) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.status = status;
    }

    public Register(Long userId,
                    Long gameId,
                    RegisterStatus status
    ) {
        this.userId = userId;
        this.gameId = gameId;
        this.status = status;
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public Long gameId() {
        return gameId;
    }

    public RegisterStatus status() {
        return status;
    }

    public void cancelRegister() {
        status.changeToCanceled();
    }

    public void acceptRegister() {
        status.changeToAccepted();
    }

    public void rejectRegister() {
        status.changeToRejected();
    }

    public static List<Register> fakeMembers(long generationCount, long gameId) {
        List<Register> members = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Register member = new Register(
                id,
                id,
                gameId,
                new RegisterStatus(RegisterStatus.ACCEPTED)
            );
            members.add(member);
        }
        return members;
    }

    public static List<Register> fakeApplicants(long generationCount, long gameId) {
        List<Register> members = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Register member = new Register(
                id,
                id,
                gameId,
                new RegisterStatus(RegisterStatus.PROCESSING)
            );
            members.add(member);
        }
        return members;
    }
}
