package kr.megaptera.smash.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDateTime;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.RegisterStatus;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetPostsServiceTest {
    private GetPostsService getPostsService;

    private PostRepository postRepository;
    private GameRepository gameRepository;
    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        gameRepository = mock(GameRepository.class);
        registerRepository = mock(RegisterRepository.class);
        getPostsService = new GetPostsService(
                postRepository,
                gameRepository,
                registerRepository
        );
    }

    @Test
    void posts() {
        // 이런 코드를 쓰고 있으면 매우 고틍스러워야 합니다. 이건 유지보수하는 게 불가능해요.
        // 테스트 코드가 있음으로 인해서 오히려 유지보수할 수 없게 된, 발목만 잡는 형국입니다.

        List<Post> posts = List.of(
                new Post(
                        1L,
                        1L,
                        new PostHits(10L),
                        new PostDetail("게시글 내용 1"),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ),
                new Post(
                        2L,
                        2L,
                        new PostHits(11L),
                        new PostDetail("게시글 내용 2"),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        List<Game> games = List.of(
                new Game(
                        1L,
                        1L,
                        new Exercise("운동 이름 1"),
                        new GameDateTime(
                                LocalDate.of(2022, 10, 13),
                                LocalTime.of(14, 0),
                                LocalTime.of(17, 0)
                        ),
                        new Place("장소 이름 1"),
                        new GameTargetMemberCount(10)
                ),
                new Game(
                        2L,
                        2L,
                        new Exercise("운동 이름 2"),
                        new GameDateTime(
                                LocalDate.of(2022, 10, 14),
                                LocalTime.of(14, 0),
                                LocalTime.of(17, 0)
                        ),
                        new Place("장소 이름 2"),
                        new GameTargetMemberCount(8)
                )
        );
        List<List<Register>> registersOfGames = List.of(
                List.of(
                        new Register(
                                1L,
                                1L,
                                1L,
                                RegisterStatus.accepted()
                        ),
                        new Register(
                                2L,
                                2L,
                                1L,
                                RegisterStatus.accepted()
                        )
                ),
                List.of(
                        new Register(
                                3L,
                                2L,
                                2L,
                                RegisterStatus.accepted()
                        ),
                        new Register(
                                4L,
                                3L,
                                2L,
                                RegisterStatus.accepted()
                        )
                )
        );

        Long accessedUserId = 1L;

        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(1L)).willReturn(Optional.of(games.get(0)));
        given(gameRepository.findByPostId(2L)).willReturn(Optional.of(games.get(1)));
        given(registerRepository.findAllByGameId(1L)).willReturn(registersOfGames.get(0));
        given(registerRepository.findAllByGameId(2L)).willReturn(registersOfGames.get(1));
        given(registerRepository.findAllByGameIdAndUserId(1L, accessedUserId))
                .willReturn(List.of(registersOfGames.get(0).get(0)));
        given(registerRepository.findAllByGameIdAndUserId(2L, accessedUserId))
                .willReturn(List.of());

        PostsDto postsDto = getPostsService.findAll(accessedUserId);

        assertThat(postsDto).isNotNull();

        List<PostListDto> postListDtos = postsDto.getPosts();
        assertThat(postListDtos.get(0).getId()).isEqualTo(1L);
        assertThat(postListDtos.get(0).getGame().getType()).isEqualTo("운동 이름 1");
        assertThat(postListDtos.get(0).getGame().getCurrentMemberCount()).isEqualTo(2);
        assertThat(postListDtos.get(0).getGame().getRegisterStatus()).isEqualTo("accepted");
        assertThat(postListDtos.get(1).getHits()).isEqualTo(11L);
        assertThat(postListDtos.get(1).getGame().getPlace()).isEqualTo("장소 이름 2");
        assertThat(postListDtos.get(1).getGame().getCurrentMemberCount()).isEqualTo(2);
        assertThat(postListDtos.get(1).getGame().getRegisterStatus()).isEqualTo("none");
    }
}
