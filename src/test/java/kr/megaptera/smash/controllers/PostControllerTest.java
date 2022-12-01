package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.PostAndGameRequestDto;
import kr.megaptera.smash.exceptions.CreatePostFailed;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
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
    private RegisterRepository registerRepository;

    @MockBean
    private UserRepository userRepository;

    private List<Post> posts;
    private List<Game> games;
    private List<Register> registersGame1;
    private List<Register> registersGame2;

    private PostAndGameRequestDto postAndGameRequestDto;
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
        postAndGameRequestDto = new PostAndGameRequestDto(
            "운동 이름",
            "2022-11-25T00:00:00.000Z",
            "am", "09", "00", "pm", "12", "50",
            "운동 장소",
            gameTargetMemberCount,
            "게시물 상세 내용"
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
    void postsWithNotLoggedin() throws Exception {
        Long currentUserId = null;

        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(posts.get(0).id()))
            .willReturn(Optional.of(games.get(0)));
        given(gameRepository.findByPostId(posts.get(1).id()))
            .willReturn(Optional.of(games.get(1)));
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
//        given(getPostsService.findAll(invalidUserId))
//            .willThrow(UserNotFound.class);
        // SpyBean 어노테이션을 주면 given이 먹히지 않는 건가...?
        // 그보다도 ExceptionHandler가 있는데 왜 예외를 받지 못하는지 모르겠다.

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

    private void createPostNormal(
        Long userId,
        String gameExercise,
        String gameDate,
        String gameStartTimeAmPm,
        String gameStartHour,
        String gameStartMinute,
        String gameEndTimeAmPm,
        String gameEndHour,
        String gameEndMinute,
        String gamePlace,
        Integer gameTargetMemberCount,
        String postDetail
    ) throws Exception {
        given(createPostService.createPost(
            userId,
            gameExercise,
            gameDate,
            gameStartTimeAmPm,
            gameStartHour,
            gameStartMinute,
            gameEndTimeAmPm,
            gameEndHour,
            gameEndMinute,
            gamePlace,
            gameTargetMemberCount,
            postDetail
        )).willReturn(createPostAndGameResultDto);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"gameExercise\":\"" + gameExercise + "\"," +
                    "\"gameDate\":\"" + gameDate + "\"," +
                    "\"gameStartTimeAmPm\":\"" + gameStartTimeAmPm + "\"," +
                    "\"gameStartHour\":\"" + gameStartHour + "\"," +
                    "\"gameStartMinute\":\"" + gameStartMinute + "\"," +
                    "\"gameEndTimeAmPm\":\"" + gameEndTimeAmPm + "\"," +
                    "\"gameEndHour\":\"" + gameEndHour + "\"," +
                    "\"gameEndMinute\":\"" + gameEndMinute + "\"," +
                    "\"gamePlace\":\"" + gamePlace + "\"," +
                    "\"gameTargetMemberCount\":" + gameTargetMemberCount + "," +
                    "\"postDetail\":\"" + postDetail + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"postId\":11")
            ))
        ;
    }

    private void createPostWithCreatePostFailed(
        Long userId,
        String gameExercise,
        String gameDate,
        String gameStartTimeAmPm,
        String gameStartHour,
        String gameStartMinute,
        String gameEndTimeAmPm,
        String gameEndHour,
        String gameEndMinute,
        String gamePlace,
        Integer gameTargetMemberCount,
        String postDetail,
        String errorMessage
    ) throws Exception {
        given(createPostService.createPost(
            userId,
            gameExercise,
            gameDate,
            gameStartTimeAmPm,
            gameStartHour,
            gameStartMinute,
            gameEndTimeAmPm,
            gameEndHour,
            gameEndMinute,
            gamePlace,
            gameTargetMemberCount,
            postDetail
        )).willThrow(new CreatePostFailed(errorMessage));

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"gameExercise\":\"" + gameExercise + "\"," +
                    "\"gameDate\":\"" + gameDate + "\"," +
                    "\"gameStartTimeAmPm\":\"" + gameStartTimeAmPm + "\"," +
                    "\"gameStartHour\":\"" + gameStartHour + "\"," +
                    "\"gameStartMinute\":\"" + gameStartMinute + "\"," +
                    "\"gameEndTimeAmPm\":\"" + gameEndTimeAmPm + "\"," +
                    "\"gameEndHour\":\"" + gameEndHour + "\"," +
                    "\"gameEndMinute\":\"" + gameEndMinute + "\"," +
                    "\"gamePlace\":\"" + gamePlace + "\"," +
                    "\"gameTargetMemberCount\":" + gameTargetMemberCount + "," +
                    "\"postDetail\":\"" + postDetail + "\"" +
                    "}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString(errorMessage)
            ))
        ;
    }

    @Test
    void createPost() throws Exception {
        Long userId = 1L;

        createPostNormal(
            userId,
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
    }

    @Test
    void createPostWithBlankGameExercise() throws Exception {
        Long userId = 1L;
        String blankGameExercise = "";
        String errorMessage = "운동을 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
            blankGameExercise,
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameStartTimeAmPm(),
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }

    @Test
    void createPostWithBlankGameDate() throws Exception {
        Long userId = 1L;
        String blankGameDate = "";
        String errorMessage = "운동 날짜를 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
            postAndGameRequestDto.getGameExercise(),
            blankGameDate,
            postAndGameRequestDto.getGameStartTimeAmPm(),
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }

    @Test
    void createPostWithBlankGameStartTimeAmPm() throws Exception {
        Long userId = 1L;
        String blankGameStartTimeAmPm = "";
        String errorMessage = "시작시간 오전/오후 구분을 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            blankGameStartTimeAmPm,
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }

    @Test
    void createPostWithBlankGameStartHour() throws Exception {
        Long userId = 1L;
        String blankGameStartHour = "";
        String errorMessage = "시작 시간을 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameStartTimeAmPm(),
            blankGameStartHour,
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }

    @Test
    void createPostWithBlankGamePlace() throws Exception {
        Long userId = 1L;
        String blankGamePlace = "";
        String errorMessage = "운동 장소 이름을 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameStartTimeAmPm(),
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            blankGamePlace,
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }

    @Test
    void createPostWithNullGameTargetMemberCount() throws Exception {
        Long userId = 1L;
        Integer nullGameTargetMemberCount = null;
        String errorMessage = "사용자 수를 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameStartTimeAmPm(),
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            nullGameTargetMemberCount,
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }

    @Test
    void createPostWithBlankPostDetail() throws Exception {
        Long userId = 1L;
        String blankPostDetail = "";
        String errorMessage = "게시물 상세 내용을 입력해주세요.";
        createPostWithCreatePostFailed(
            userId,
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
            blankPostDetail,
            errorMessage
        );
    }

    @Test
    void createPostWithUserNotFound() throws Exception {
        Long userId = 1L;
        given(createPostService.createPost(
            userId,
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
        )).willThrow(UserNotFound.class);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"gameExercise\":\""
                    + postAndGameRequestDto.getGameExercise() + "\"," +
                    "\"gameDate\":\""
                    + postAndGameRequestDto.getGameDate() + "\"," +
                    "\"gameStartTimeAmPm\":\""
                    + postAndGameRequestDto.getGameStartTimeAmPm() + "\"," +
                    "\"gameStartHour\":\""
                    + postAndGameRequestDto.getGameStartHour() + "\"," +
                    "\"gameStartMinute\":\""
                    + postAndGameRequestDto.getGameStartMinute() + "\"," +
                    "\"gameEndTimeAmPm\":\""
                    + postAndGameRequestDto.getGameEndTimeAmPm() + "\"," +
                    "\"gameEndHour\":\""
                    + postAndGameRequestDto.getGameEndHour() + "\"," +
                    "\"gameEndMinute\":\""
                    + postAndGameRequestDto.getGameEndMinute() + "\"," +
                    "\"gamePlace\":\""
                    + postAndGameRequestDto.getGamePlace() + "\"," +
                    "\"gameTargetMemberCount\":"
                    + postAndGameRequestDto.getGameTargetMemberCount() + "," +
                    "\"postDetail\":\""
                    + postAndGameRequestDto.getPostDetail() + "\"" +
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
