package kr.megaptera.smash.models.register;

import kr.megaptera.smash.models.game.Game;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.models.notice.NoticeContents;
import kr.megaptera.smash.models.notice.NoticeStatus;
import kr.megaptera.smash.models.user.User;

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

    public Register(User user,
                    Game game
    ) {
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

    public RegisterStatus status() {
        return status;
    }

    public boolean match(User user) {
        return user != null && userId.equals(user.id());
    }

    public boolean match(Long userId) {
        return this.userId.equals(userId);
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

    public void cancel() {
        status = RegisterStatus.canceled();
    }

    public void accept() {
        status = RegisterStatus.accepted();
    }

    public void reject() {
        status = RegisterStatus.rejected();
    }

    public Notice createRegisterNotice(User currentUser,
                                       User postAuthor) {
        return new Notice(
            postAuthor.id(),
            new NoticeContents(
                "????????? ?????? ???????????? ????????? ????????? ?????????????????????.",
                "????????? ?????????: " + currentUser.personalInformation().name()
            ),
            NoticeStatus.unread()
        );
    }

    public Notice createAcceptNotice(User user, Game game) {
        return new Notice(
            user.id(),
            new NoticeContents(
                "????????? ?????? ?????? ???????????? ????????? ?????????????????????.",
                "????????? ?????? ??????: " + game.exercise() + "\n"
                + "????????? ?????? ??????: " + game.dateTime().joinDateAndTime()
            ),
            NoticeStatus.unread()
        );
    }

    // TODO: UserId, GameId ??? ????????? ????????? Long?????? ???????????? ??????????????? ??????

    public static Register fake(Long userId, Long gameId) {
        return new Register(
            userId,
            gameId,
            RegisterStatus.processing()
        );
    }

    public static Register fake(Long userId,
                                Long gameId,
                                RegisterStatus status) {
        return new Register(
            userId,
            gameId,
            status
        );
    }

    public static Register fakeProcessing(Long userId, Long gameId) {
        return new Register(
            userId,
            gameId,
            RegisterStatus.processing()
        );
    }

    public static Register fakeCanceled(Long userId, Long gameId) {
        return new Register(
            userId,
            gameId,
            RegisterStatus.canceled()
        );
    }

    public static Register fakeAccepted(Long userId, Long gameId) {
        return new Register(
            userId,
            gameId,
            RegisterStatus.accepted()
        );
    }

    public static Register fakeRejected(Long userId, Long gameId) {
        return new Register(
            userId,
            gameId,
            RegisterStatus.rejected()
        );
    }

    public static List<Register> fakesAccepted(long generationCount,
                                               long gameId) {
        List<Register> registers = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Long userId = id;
            Register register = new Register(
                id,
                userId,
                gameId,
                RegisterStatus.accepted()
            );
            registers.add(register);
        }
        return registers;
    }

    public static List<Register> fakesProcessing(long generationCount,
                                                 long gameId) {
        List<Register> registers = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Long userId = id;
            Register register = new Register(
                id,
                userId,
                gameId,
                RegisterStatus.processing()
            );
            registers.add(register);
        }
        return registers;
    }
}
