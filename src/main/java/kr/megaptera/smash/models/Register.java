package kr.megaptera.smash.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public Register(User user, Game game) {
        this.userId = user.id();
        this.gameId = game.id();
        this.status = RegisterStatus.processing();
    }

    public Register(Long userId,
                    Long gameId,
                    RegisterStatus status
    ) {
        this.userId = userId;
        this.gameId = gameId;
        this.status = status;
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

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public Long gameId() {
        return gameId;
    }

    public boolean match(User user) {
        return user != null && userId.equals(user.id());
    }

    public boolean active() {
        return status.equals(RegisterStatus.processing())
                || status.equals(RegisterStatus.accepted());
    }

    public boolean accepted() {
        return status.equals(RegisterStatus.accepted());
    }

    public boolean processing() {
        return status.equals(RegisterStatus.processing());
    }

    public RegisterStatus status() {
        return status;
    }

    public void cancelRegister() {
        status = RegisterStatus.canceled();
    }

    public void acceptRegister() {
        status = RegisterStatus.accepted();
    }

    public void rejectRegister() {
        status = RegisterStatus.rejected();
    }

    // UserId, GameId란 VO가 필요하다는 강력한 신호입니다.

    public static Register fake(Long userId, Long gameId) {
        return new Register(userId, gameId, RegisterStatus.accepted());
    }

    public static Register fake(RegisterStatus status) {
        return new Register(1L, 1L, status);
    }

    public static List<Register> fakeMembers(long generationCount, long gameId) {
        List<Register> members = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Register member = new Register(
                    id,
                    id,
                    gameId,
                    RegisterStatus.accepted()
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
                    RegisterStatus.processing()
            );
            members.add(member);
        }
        return members;
    }
}
