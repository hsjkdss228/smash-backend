package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDate;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                new GameDate("2022년 10월 13일 14:00~17:00"),
                new Place("장소 이름 1"),
                new GameTargetMemberCount(10)
            ),
            new Game(
                2L,
                2L,
                new Exercise("운동 이름 2"),
                new GameDate("2022년 10월 14일 14:00~17:00"),
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
                    new RegisterStatus(RegisterStatus.ACCEPTED)
                ),
                new Register(
                    2L,
                    2L,
                    1L,
                    new RegisterStatus(RegisterStatus.ACCEPTED)
                )
            ),
            List.of(
                new Register(
                    3L,
                    2L,
                    2L,
                    new RegisterStatus(RegisterStatus.ACCEPTED)
                ),
                new Register(
                    4L,
                    3L,
                    2L,
                    new RegisterStatus(RegisterStatus.ACCEPTED)
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
