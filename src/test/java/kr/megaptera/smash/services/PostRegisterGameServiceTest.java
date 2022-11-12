package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.MemberName;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostRegisterGameServiceTest {
    private GameRepository gameRepository;
    private MemberRepository memberRepository;
    private UserRepository userRepository;

    private PostRegisterGameService postRegisterGameService;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        memberRepository = mock(MemberRepository.class);
        userRepository = mock(UserRepository.class);

        postRegisterGameService = new PostRegisterGameService(
            gameRepository,
            memberRepository,
            userRepository
        );
    }

    @Test
    void registerGame() {
        Long userId = 1L;
        Long gameId = 1L;
        Game game = Game.fake(1L, 1L);
        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        List<Member> members = List.of(
            // TODO: 신청 작업 백엔드 마무리되면 즉시 사용되는 객체들을 값 객체로 변환!!!!
            // id, userId, gameId
            Member.fake(1L, 2L, gameId),
            Member.fake(2L, 3L, gameId)
        );
        given(memberRepository.findByGameId(gameId)).willReturn(members);

        User user = User.fake(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Member registeredMember = Member.fake(3L, userId, gameId, new MemberName(user.name().value()));
        given(memberRepository.save(any(Member.class))).willReturn(registeredMember);

        RegisterGameResultDto registerGameResultDto
            = postRegisterGameService.registerGame(gameId, userId);

        assertThat(registerGameResultDto).isNotNull();
        assertThat(registerGameResultDto.getGameId()).isEqualTo(gameId);

        verify(gameRepository).findById(gameId);
        verify(memberRepository).findByGameId(gameId);
        verify(memberRepository).save(any(Member.class));
    }
}
