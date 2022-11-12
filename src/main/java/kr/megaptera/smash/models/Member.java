package kr.megaptera.smash.models;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long gameId;

    @Embedded
    private MemberName name;

    private Member() {

    }

    public Member(Long id,
                  Long userId,
                  Long gameId,
                  MemberName name
    ) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.name = name;
    }

    public Member(Long userId,
                  Long gameId,
                  MemberName name
    ) {
        this.userId = userId;
        this.gameId = gameId;
        this.name = name;
    }

    public Long userId() {
        return userId;
    }

    public Long gameId() {
        return gameId;
    }

    public static List<Member> fakes(long generationCount, long gameId) {
        List<Member> members = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Member member = new Member(
                id,
                id,
                gameId,
                new MemberName("참가자명")
            );
            members.add(member);
        }
        return members;
    }
}
