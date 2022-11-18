package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDate;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetGameServiceTest {
    private GetGameService getGameService;

    private GameRepository gameRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        memberRepository = mock(MemberRepository.class);

        getGameService = new GetGameService(gameRepository, memberRepository);
    }

    @Test
    void findTargetGame() {
        Long gameId = 1L;
        Long postId = 1L;
        Game game = new Game(
            gameId,
            postId,
            new Exercise("야구"),
            new GameDate("2022년 8월 21일 19:00~22:00"),
            new Place("목동야구장"),
            new GameTargetMemberCount(10)
        );
        List<Member> members = Member.fakes(8, 1L);
        Long targetPostId = 1L;
        Long accessedUserId = 10L;

        given(gameRepository.findByPostId(targetPostId))
            .willReturn(Optional.of(game));
        given(memberRepository.findAllByGameId(game.id()))
            .willReturn(members);

        GameDetailDto gameDetailDto
            = getGameService.findTargetGame(accessedUserId, targetPostId);

        assertThat(gameDetailDto).isNotNull();
        assertThat(gameDetailDto.getCurrentMemberCount()).isEqualTo(8);
        assertThat(gameDetailDto.getIsRegistered()).isFalse();
    }
}
