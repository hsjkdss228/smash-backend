package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.ExerciseForPostCreateRequestDto;
import kr.megaptera.smash.dtos.GameForPostCreateRequestDto;
import kr.megaptera.smash.dtos.PlaceForPostCreateRequestDto;
import kr.megaptera.smash.dtos.PostCreateRequestDto;
import kr.megaptera.smash.dtos.PostForPostCreateRequestDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDateTime;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
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
    private PlaceRepository placeRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private RegisterRepository registerRepository;

    private CreatePostService createPostService;

    private PostForPostCreateRequestDto postForPostCreateRequestDto;
    private GameForPostCreateRequestDto gameForPostCreateRequestDto;
    private ExerciseForPostCreateRequestDto exerciseForPostCreateRequestDto;
    private PlaceForPostCreateRequestDto placeForPostCreateRequestDto;

    private Post savedPost;
    private Long savedPostId;
    private User user;
    private Game savedGame;
    private Place place;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        placeRepository = mock(PlaceRepository.class);
        gameRepository = mock(GameRepository.class);
        userRepository = mock(UserRepository.class);
        registerRepository = mock(RegisterRepository.class);

        createPostService = new CreatePostService(
            postRepository,
            placeRepository,
            gameRepository,
            userRepository,
            registerRepository
        );

        place = Place.fake("운동 장소 이름");
        Integer gameTargetMemberCount = 20;
        postForPostCreateRequestDto = new PostForPostCreateRequestDto(
            "게시물 상세 내용"
        );
        gameForPostCreateRequestDto = new GameForPostCreateRequestDto(
            "2022-12-22T00:00:00.000Z",
            "am", "11", "30", "pm", "04", "00",
            gameTargetMemberCount
        );
        exerciseForPostCreateRequestDto = new ExerciseForPostCreateRequestDto(
            "운동 이름"
        );
        placeForPostCreateRequestDto = new PlaceForPostCreateRequestDto(
            place.information().name()
        );

        user = User.fake("치코리타", "chikorita12");
        savedPostId = 5L;
        savedPost = new Post(
            savedPostId,
            user.id(),
            new PostHits(0L),
            new PostDetail(postForPostCreateRequestDto.getDetail())
        );
        Long createdGameId = 12L;
        savedGame = new Game(
            createdGameId,
            savedPost.id(),
            place.id(),
            new Exercise(exerciseForPostCreateRequestDto.getName()),
            new GameDateTime(
                LocalDate.of(2022, 12, 22),
                LocalTime.of(11, 30),
                LocalTime.of(16, 0)
            ),
            new GameTargetMemberCount(
                gameForPostCreateRequestDto.getTargetMemberCount()
            )
        );
    }

    @Test
    void createPost() {
        given(userRepository.findById(user.id()))
            .willReturn(Optional.of(user));
        given(placeRepository.findByInformationName(place.information().name()))
            .willReturn(Optional.of(place));
        given(postRepository.save(any(Post.class)))
            .willReturn(savedPost);
        given(gameRepository.save(any(Game.class)))
            .willReturn(savedGame);

        CreatePostAndGameResultDto createPostAndGameResultDto
            = createPostService.createPost(
            user.id(),
            postForPostCreateRequestDto,
            gameForPostCreateRequestDto,
            exerciseForPostCreateRequestDto,
            placeForPostCreateRequestDto
        );

        assertThat(createPostAndGameResultDto).isNotNull();
        assertThat(createPostAndGameResultDto.getPostId())
            .isEqualTo(savedPostId);

        verify(userRepository).findById(user.id());
        verify(postRepository).save(any(Post.class));
        verify(gameRepository).save(any(Game.class));
        verify(registerRepository).save(any(Register.class));
    }

    @Test
    void createPostWithUserNotFound() {
        given(userRepository.findById(user.id()))
            .willThrow(UserNotFound.class);

        assertThrows(UserNotFound.class, () -> {
            createPostService.createPost(
                user.id(),
                postForPostCreateRequestDto,
                gameForPostCreateRequestDto,
                exerciseForPostCreateRequestDto,
                placeForPostCreateRequestDto
            );
        });

        verify(userRepository).findById(user.id());
    }
}
