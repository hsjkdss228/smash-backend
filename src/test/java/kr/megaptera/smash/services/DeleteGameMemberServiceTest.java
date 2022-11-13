package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.MemberName;
import kr.megaptera.smash.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DeleteGameMemberServiceTest {
    private DeleteGameMemberService deleteGameMemberService;

    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);

        deleteGameMemberService
            = new DeleteGameMemberService(memberRepository);
    }

    @Test
    void deleteGameMember() {
        Long userId = 1L;
        Long gameId = 1L;
        Member member = new Member(1L, userId, gameId, new MemberName("삭제될 사용자"));

        given(memberRepository.findByGameIdAndUserId(gameId, userId))
            .willReturn(Optional.of(member));

        deleteGameMemberService.deleteGameMember(userId, gameId);

        verify(memberRepository).delete(any(Member.class));
    }
}
