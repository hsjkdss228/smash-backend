package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostAndGameRequestDto;
import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.CreatePostFailed;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.CreatePostService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@MockMvcEncoding
@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // GET posts
    @MockBean
    private GetPostsService getPostsService;

    private List<PostListDto> postListDtos;
    private PostsDto postsDto;

    // GET post
    @MockBean
    private GetPostService getPostService;

    private PostDetailDto postDetailDto;

    // POST posts
    @MockBean
    private CreatePostService createPostService;

    private PostAndGameRequestDto postAndGameRequestDto;
    private CreatePostAndGameResultDto createPostAndGameResultDto;

    // stubs
    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long userId = 1L;
    private Long targetPostId = 1L;
    private Long createdPostId = 11L;

    @BeforeEach
    void setUp() {
        token = jwtUtil.encode(userId);

        Boolean isAuthor = true;
        Boolean isNotAuthor = false;
        Long notRegisteredRegisterId = -1L;
        String notRegisteredRegisterStatus = "none";
        Long processingRegisteredId = 1L;
        String processingRegisterStatus = "processing";

        // GET posts
        long generationCount = 2;
        List<Post> posts = Post.fakes(generationCount);
        List<Game> games = Game.fakes(generationCount);
        List<List<Register>> membersOfGames = new ArrayList<>();
        for (long gameId = 1; gameId <= generationCount; gameId += 1) {
            List<Register> members = Register.fakeMembers(generationCount, gameId);
            membersOfGames.add(members);
        }

        postListDtos = List.of(
            new PostListDto(
                posts.get(0).id(),
                posts.get(0).hits().value(),
                isAuthor,
                new GameInPostListDto(
                    games.get(0).id(),
                    games.get(0).exercise().name(),
                    games.get(0).dateTime().joinDateAndTime(),
                    games.get(0).place().name(),
                    membersOfGames.get(0).size(),
                    games.get(0).targetMemberCount().value(),
                    notRegisteredRegisterId,
                    notRegisteredRegisterStatus
                )
            ),
            new PostListDto(
                posts.get(1).id(),
                posts.get(1).hits().value(),
                isNotAuthor,
                new GameInPostListDto(
                    games.get(1).id(),
                    games.get(1).exercise().name(),
                    games.get(1).dateTime().joinDateAndTime(),
                    games.get(1).place().name(),
                    membersOfGames.get(1).size(),
                    games.get(1).targetMemberCount().value(),
                    processingRegisteredId,
                    processingRegisterStatus
                )
            )
        );
        postsDto = new PostsDto(postListDtos);


        // GET post
        Post post = Post.fake("주말 오전 테니스 같이하실 여성분들 찾습니다.");
        User user = User.fake("The Prince of the Tennis", "PrinceOfTennis1234");
        postDetailDto = new PostDetailDto(
            post.id(),
            post.hits().value(),
            user.name().value(),
            user.phoneNumber().value(),
            post.detail().value(),
            isAuthor
        );

        // POST post
        Integer gameTargetMemberCount = 10;
        postAndGameRequestDto = new PostAndGameRequestDto(
            "운동 이름",
            "2022-11-25T00:00:00.000Z",
            "am", "09", "00", "pm", "12", "50",
            "운동 장소",
            gameTargetMemberCount,
            "게시물 상세 내용"
        );
        createPostAndGameResultDto
            = new CreatePostAndGameResultDto(createdPostId);
    }

    @Test
    void posts() throws Exception {
        given(getPostsService.findAll(userId)).willReturn(postsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"hits\":123")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"game\":{\"id\":1")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"currentMemberCount\":2")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"registerStatus\":\"none\"")
            ))
        ;
    }

    @Test
    void postsFailed() throws Exception {
        given(getPostsService.findAll(userId))
            .willThrow(PostsFailed.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("errorMessage")
            ))
        ;
    }

    @Test
    void post() throws Exception {
        given(getPostService.findTargetPost(userId, targetPostId))
            .willReturn(postDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"authorName\":\"The Prince of the Tennis\"")
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

    private void createPostWithError(
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
        String blankGameExercise = "";
        String errorMessage = "운동을 입력해주세요.";
        createPostWithError(
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
        String blankGameDate = "";
        String errorMessage = "운동 날짜를 입력해주세요.";
        createPostWithError(
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
        String blankGameStartTimeAmPm = "";
        String errorMessage = "시작시간 오전/오후 구분을 입력해주세요.";
        createPostWithError(
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
        String blankGameStartHour = "";
        String errorMessage = "시작 시간을 입력해주세요.";
        createPostWithError(
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
        String blankGamePlace = "";
        String errorMessage = "운동 장소 이름을 입력해주세요.";
        createPostWithError(
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
        Integer nullGameTargetMemberCount = null;
        String errorMessage = "사용자 수를 입력해주세요.";
        createPostWithError(
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
        String blankPostDetail = "";
        String errorMessage = "게시물 상세 내용을 입력해주세요.";
        createPostWithError(
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
        String errorMessage = "접속한 사용자를 찾을 수 없습니다.";
        createPostWithError(
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
            postAndGameRequestDto.getPostDetail(),
            errorMessage
        );
    }
}
