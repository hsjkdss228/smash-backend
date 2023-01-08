package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.ExerciseForPostCreateRequestDto;
import kr.megaptera.smash.dtos.GameForPostCreateRequestDto;
import kr.megaptera.smash.dtos.PlaceForPostCreateRequestDto;
import kr.megaptera.smash.dtos.PostCreateRequestDto;
import kr.megaptera.smash.dtos.PostForPostCreateRequestDto;
import kr.megaptera.smash.exceptions.CreatePostFailed;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.game.Game;
import kr.megaptera.smash.models.game.GameTargetMemberCount;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.models.post.Post;
import kr.megaptera.smash.models.register.Register;
import kr.megaptera.smash.models.user.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import kr.megaptera.smash.services.CreatePostService;
import kr.megaptera.smash.services.DeletePostService;
import kr.megaptera.smash.services.GetPostService;
import kr.megaptera.smash.services.GetPostsService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MockMvcEncoding
@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private GetPostsService getPostsService;

    @SpyBean
    private GetPostService getPostService;

    @MockBean
    private CreatePostService createPostService;

    @MockBean
    private DeletePostService deletePostService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private PlaceRepository placeRepository;

    @MockBean
    private RegisterRepository registerRepository;

    @MockBean
    private UserRepository userRepository;

    private List<Post> posts;
    private List<Game> games;
    private List<Place> places;
    private List<Register> registersGame1;
    private List<Register> registersGame2;

    private PostForPostCreateRequestDto postForPostCreateRequestDto;
    private GameForPostCreateRequestDto gameForPostCreateRequestDto;
    private ExerciseForPostCreateRequestDto exerciseForPostCreateRequestDto;
    private PlaceForPostCreateRequestDto placeForPostCreateRequestDto;
    private PostCreateRequestDto postCreateRequestDto;
    private CreatePostAndGameResultDto createPostAndGameResultDto;

    @SpyBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        int generationCount = 2;
        posts = Post.fakes(generationCount);
        games = Game.fakes(
            generationCount,
            new GameTargetMemberCount(3));
        places = Place.fakes(generationCount);
        registersGame1 = List.of(
            Register.fakeAccepted(1L, games.get(0).id()),
            Register.fakeProcessing(2L, games.get(0).id()),
            Register.fakeAccepted(3L, games.get(0).id()),
            Register.fakeCanceled(4L, games.get(0).id())
        );
        registersGame2 = List.of(
            Register.fakeRejected(5L, games.get(1).id()),
            Register.fakeAccepted(6L, games.get(1).id()),
            Register.fakeAccepted(7L, games.get(1).id()),
            Register.fakeAccepted(8L, games.get(1).id())
        );

        Integer gameTargetMemberCount = 10;
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
            "운동 장소",
            "운동 장소 주소",
            false
        );
        postCreateRequestDto = new PostCreateRequestDto(
            postForPostCreateRequestDto,
            gameForPostCreateRequestDto,
            exerciseForPostCreateRequestDto,
            placeForPostCreateRequestDto
        );
        Long createdPostId = 11L;
        createPostAndGameResultDto
            = new CreatePostAndGameResultDto(createdPostId);
    }

    @Test
    void posts() throws Exception {
        Long currentUserId = 1L;
        User user = User.fake(currentUserId);

        given(userRepository.findById(currentUserId))
            .willReturn(Optional.of(user));
        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(posts.get(0).id()))
            .willReturn(Optional.of(games.get(0)));
        given(gameRepository.findByPostId(posts.get(1).id()))
            .willReturn(Optional.of(games.get(1)));
        given(placeRepository.findById(games.get(0).placeId()))
            .willReturn(Optional.of(places.get(0)));
        given(placeRepository.findById(games.get(1).placeId()))
            .willReturn(Optional.of(places.get(1)));
        given(registerRepository.findAllByGameId(games.get(0).id()))
            .willReturn(registersGame1);
        given(registerRepository.findAllByGameId(games.get(1).id()))
            .willReturn(registersGame2);

        String token = jwtUtil.encode(currentUserId);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"game\":{\"id\":2")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"currentMemberCount\":3")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"isAuthor\":true")
            ))
        ;
    }

    @Test
    void postsWithNotLoggedIn() throws Exception {
        Long currentUserId = null;

        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(posts.get(0).id()))
            .willReturn(Optional.of(games.get(0)));
        given(gameRepository.findByPostId(posts.get(1).id()))
            .willReturn(Optional.of(games.get(1)));
        given(placeRepository.findById(games.get(0).placeId()))
            .willReturn(Optional.of(places.get(0)));
        given(placeRepository.findById(games.get(1).placeId()))
            .willReturn(Optional.of(places.get(1)));
        given(registerRepository.findAllByGameId(games.get(0).id()))
            .willReturn(registersGame1);
        given(registerRepository.findAllByGameId(games.get(1).id()))
            .willReturn(registersGame2);

        String token = jwtUtil.encode(currentUserId);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"isAuthor\":false")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"registerId\":null")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"registerStatus\":\"none\"")
            ))
        ;

        verify(userRepository, never()).findById(any(Long.class));
    }

    @Test
    void postsWithUserNotFound() throws Exception {
        Long invalidUserId = 10L;
        given(userRepository.findById(invalidUserId))
            .willThrow(UserNotFound.class);

        String token = jwtUtil.encode(invalidUserId);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void postsWithGameNotFound() throws Exception {
        Long userId = 1L;
        Long invalidGameId = 10L;
        given(gameRepository.findById(invalidGameId))
            .willThrow(GameNotFound.class);
//        given(getPostsService.findAll(invalidUserId))
//            .willThrow(GameNotFound.class);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void post() throws Exception {
        Long targetPostId = 1L;
        Post post = Post.fake(targetPostId);
        given(postRepository.findById(targetPostId))
            .willReturn(Optional.of(post));

        Long currentUserId = 1L;
        User user = User.fake(currentUserId);
        given(userRepository.findById(currentUserId))
            .willReturn(Optional.of(user));

        String token = jwtUtil.encode(currentUserId);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"isAuthor\":true")
            ))
        ;
    }

    @Test
    void createPost() throws Exception {
        Long userId = 1L;
        given(createPostService.createPost(
            eq(userId),
            any(PostForPostCreateRequestDto.class),
            any(GameForPostCreateRequestDto.class),
            any(ExerciseForPostCreateRequestDto.class),
            any(PlaceForPostCreateRequestDto.class)
        )).willReturn(createPostAndGameResultDto);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"post\":{" +
                    "\"detail\":\"" + postForPostCreateRequestDto.getDetail() + "\"" +
                    "}," +
                    "\"game\":{" +
                    "\"date\":\"" + gameForPostCreateRequestDto.getDate() + "\"," +
                    "\"startTimeAmPm\":\"" + gameForPostCreateRequestDto.getStartTimeAmPm() + "\"," +
                    "\"startHour\":\"" + gameForPostCreateRequestDto.getStartHour() + "\"," +
                    "\"startMinute\":\"" + gameForPostCreateRequestDto.getStartMinute() + "\"," +
                    "\"endTimeAmPm\":\"" + gameForPostCreateRequestDto.getEndTimeAmPm() + "\"," +
                    "\"endHour\":\"" + gameForPostCreateRequestDto.getEndHour() + "\"," +
                    "\"endMinute\":\"" + gameForPostCreateRequestDto.getEndMinute() + "\"," +
                    "\"targetMemberCount\": " + gameForPostCreateRequestDto.getTargetMemberCount() +
                    "}," +
                    "\"exercise\":{" +
                    "\"name\":\"" + exerciseForPostCreateRequestDto.getName() + "\"" +
                    "}," +
                    "\"place\":{" +
                    "\"name\":\"" + placeForPostCreateRequestDto.getName() + "\"" +
                    "}" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"postId\":11")
            ))
        ;
    }

    @Test
    void createPostWithBlankGameExercise() throws Exception {
        Long userId = 1L;
        String blankGameExercise = "";
        String errorMessage = "운동을 입력해주세요.";

        ExerciseForPostCreateRequestDto wrongExerciseForPostCreateRequestDto
            = new ExerciseForPostCreateRequestDto(
            blankGameExercise
        );
        given(createPostService.createPost(
            userId,
            postForPostCreateRequestDto,
            gameForPostCreateRequestDto,
            wrongExerciseForPostCreateRequestDto,
            placeForPostCreateRequestDto
        )).willThrow(new CreatePostFailed(errorMessage));

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"post\":{" +
                    "\"detail\":\"" + postForPostCreateRequestDto.getDetail() + "\"" +
                    "}," +
                    "\"game\":{" +
                    "\"date\":\"" + gameForPostCreateRequestDto.getDate() + "\"," +
                    "\"startTimeAmPm\":\"" + gameForPostCreateRequestDto.getStartTimeAmPm() + "\"," +
                    "\"startHour\":\"" + gameForPostCreateRequestDto.getStartHour() + "\"," +
                    "\"startMinute\":\"" + gameForPostCreateRequestDto.getStartMinute() + "\"," +
                    "\"endTimeAmPm\":\"" + gameForPostCreateRequestDto.getEndTimeAmPm() + "\"," +
                    "\"endHour\":\"" + gameForPostCreateRequestDto.getEndHour() + "\"," +
                    "\"endMinute\":\"" + gameForPostCreateRequestDto.getEndMinute() + "\"," +
                    "\"targetMemberCount\": " + gameForPostCreateRequestDto.getTargetMemberCount() +
                    "}," +
                    "\"exercise\":{" +
                    "\"name\":\"" + wrongExerciseForPostCreateRequestDto.getName() + "\"" +
                    "}," +
                    "\"place\":{" +
                    "\"name\":\"" + placeForPostCreateRequestDto.getName() + "\"" +
                    "}" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString(errorMessage)
            ))
        ;
    }

    @Test
    void createPostWithNullGameTargetMemberCount() throws Exception {
        Long userId = 1L;
        Integer nullGameTargetMemberCount = null;
        String errorMessage = "사용자 수를 입력해주세요.";

        GameForPostCreateRequestDto wrongGameForPostCreateRequestDto
            = new GameForPostCreateRequestDto(
            "2022-12-22T00:00:00.000Z",
            "am", "11", "30", "pm", "04", "00",
            nullGameTargetMemberCount
        );
        given(createPostService.createPost(
            userId,
            postForPostCreateRequestDto,
            wrongGameForPostCreateRequestDto,
            exerciseForPostCreateRequestDto,
            placeForPostCreateRequestDto
        )).willThrow(new CreatePostFailed(errorMessage));

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"post\":{" +
                    "\"detail\":\"" + postForPostCreateRequestDto.getDetail() + "\"" +
                    "}," +
                    "\"game\":{" +
                    "\"date\":\"" + wrongGameForPostCreateRequestDto.getDate() + "\"," +
                    "\"startTimeAmPm\":\"" + wrongGameForPostCreateRequestDto.getStartTimeAmPm() + "\"," +
                    "\"startHour\":\"" + wrongGameForPostCreateRequestDto.getStartHour() + "\"," +
                    "\"startMinute\":\"" + wrongGameForPostCreateRequestDto.getStartMinute() + "\"," +
                    "\"endTimeAmPm\":\"" + wrongGameForPostCreateRequestDto.getEndTimeAmPm() + "\"," +
                    "\"endHour\":\"" + wrongGameForPostCreateRequestDto.getEndHour() + "\"," +
                    "\"endMinute\":\"" + wrongGameForPostCreateRequestDto.getEndMinute() + "\"," +
                    "\"targetMemberCount\": " + wrongGameForPostCreateRequestDto.getTargetMemberCount() +
                    "}," +
                    "\"exercise\":{" +
                    "\"name\":\"" + exerciseForPostCreateRequestDto.getName() + "\"" +
                    "}," +
                    "\"place\":{" +
                    "\"name\":\"" + placeForPostCreateRequestDto.getName() + "\"" +
                    "}" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString(errorMessage)
            ))
        ;
    }

    @Test
    void createPostWithUserNotFound() throws Exception {
        Long userId = 1L;
        given(createPostService.createPost(
            eq(userId),
            any(PostForPostCreateRequestDto.class),
            any(GameForPostCreateRequestDto.class),
            any(ExerciseForPostCreateRequestDto.class),
            any(PlaceForPostCreateRequestDto.class)
        )).willThrow(UserNotFound.class);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"post\":{" +
                    "\"detail\":\"" + postForPostCreateRequestDto.getDetail() + "\"" +
                    "}," +
                    "\"game\":{" +
                    "\"date\":\"" + gameForPostCreateRequestDto.getDate() + "\"," +
                    "\"startTimeAmPm\":\"" + gameForPostCreateRequestDto.getStartTimeAmPm() + "\"," +
                    "\"startHour\":\"" + gameForPostCreateRequestDto.getStartHour() + "\"," +
                    "\"startMinute\":\"" + gameForPostCreateRequestDto.getStartMinute() + "\"," +
                    "\"endTimeAmPm\":\"" + gameForPostCreateRequestDto.getEndTimeAmPm() + "\"," +
                    "\"endHour\":\"" + gameForPostCreateRequestDto.getEndHour() + "\"," +
                    "\"endMinute\":\"" + gameForPostCreateRequestDto.getEndMinute() + "\"," +
                    "\"targetMemberCount\": " + gameForPostCreateRequestDto.getTargetMemberCount() +
                    "}," +
                    "\"exercise\":{" +
                    "\"name\":\"" + exerciseForPostCreateRequestDto.getName() + "\"" +
                    "}," +
                    "\"place\":{" +
                    "\"name\":\"" + placeForPostCreateRequestDto.getName() + "\"" +
                    "}" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void deletePost() throws Exception {
        Long userId = 1L;
        Long targetPostId = 1L;

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deletePostService).deletePost(userId, targetPostId);
    }
}
