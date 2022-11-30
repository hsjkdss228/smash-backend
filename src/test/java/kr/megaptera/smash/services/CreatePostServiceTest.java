package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.PostAndGameRequestDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDateTime;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreatePostServiceTest {
    private PostRepository postRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;

    private CreatePostService createPostService;

    private PostAndGameRequestDto postAndGameRequestDto;
    private User user;
    private Post savedPost;
    private Long createdPostId;
    private Game savedGame;
    private Long createdGameId;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        gameRepository = mock(GameRepository.class);
        userRepository = mock(UserRepository.class);

        createPostService = new CreatePostService(
            postRepository,
            gameRepository,
            userRepository
        );

        Integer gameTargetMemberCount = 20;
        postAndGameRequestDto = new PostAndGameRequestDto(
            "운동 이름",
            "2022-12-22T00:00:00.000Z",
            "am", "11", "30", "pm", "04", "00",
            "운동 장소",
            gameTargetMemberCount,
            "게시물 상세 내용"
        );

        user = User.fake("치코리타", "chikorita12");
        createdPostId = 5L;
        savedPost = new Post(
            createdPostId,
            user.id(),
            new PostHits(0L),
            new PostDetail(postAndGameRequestDto.getPostDetail())
        );
        createdGameId = 12L;
        savedGame = new Game(
            createdGameId,
            savedPost.id(),
            new Exercise(postAndGameRequestDto.getGameExercise()),
            new GameDateTime(
                LocalDate.of(2022, 12, 22),
                LocalTime.of(11, 30),
                LocalTime.of(16, 0)
            ),
            new Place(postAndGameRequestDto.getGamePlace()),
            new GameTargetMemberCount(
                postAndGameRequestDto.getGameTargetMemberCount()
            )
        );
    }

    @Test
    void createPost() {
        given(userRepository.findById(user.id()))
            .willReturn(Optional.of(user));
        given(postRepository.save(any(Post.class)))
            .willReturn(savedPost);
        given(gameRepository.save(any(Game.class)))
            .willReturn(savedGame);

        CreatePostAndGameResultDto createPostAndGameResultDto
            = createPostService.createPost(
            user.id(),
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameStartTimeAmPm(),
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail()
        );

        assertThat(createPostAndGameResultDto).isNotNull();
        assertThat(createPostAndGameResultDto.getPostId())
            .isEqualTo(createdPostId);

        verify(userRepository).findById(user.id());
        verify(postRepository).save(any(Post.class));
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void createPostWithUserNotFound() {
        given(userRepository.findById(user.id()))
            .willThrow(UserNotFound.class);

        assertThrows(UserNotFound.class, () -> {
            createPostService.createPost(
                user.id(),
                postAndGameRequestDto.getGameExercise(),
                postAndGameRequestDto.getGameDate(),
                postAndGameRequestDto.getGameStartTimeAmPm(),
                postAndGameRequestDto.getGameStartHour(),
                postAndGameRequestDto.getGameStartMinute(),
                postAndGameRequestDto.getGameEndTimeAmPm(),
                postAndGameRequestDto.getGameEndHour(),
                postAndGameRequestDto.getGameEndMinute(),
                postAndGameRequestDto.getGamePlace(),
                postAndGameRequestDto.getGameTargetMemberCount(),
                postAndGameRequestDto.getPostDetail()
            );
        });

        verify(userRepository).findById(user.id());
    }
}
