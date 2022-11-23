package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.GetPostService;
import kr.megaptera.smash.services.GetPostsService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

    // posts
    @MockBean
    private GetPostsService getPostsService;

    private List<PostListDto> postListDtos;
    private PostsDto postsDto;

    // post
    @MockBean
    private GetPostService getPostService;

    private PostDetailDto postDetailDto;

    // stubs
    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long userId = 1L;
    private Long targetPostId = 1L;

    @BeforeEach
    void setUp() {
        token = jwtUtil.encode(userId);

        Boolean isAuthor = true;
        Boolean isNotAuthor = false;
        Long notRegisteredRegisterId = -1L;
        String notRegisteredRegisterStatus = "none";
        Long processingRegisteredId = 1L;
        String processingRegisterStatus = "processing";

        // posts
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
                    games.get(0).date().value(),
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
                    games.get(1).date().value(),
                    games.get(1).place().name(),
                    membersOfGames.get(1).size(),
                    games.get(1).targetMemberCount().value(),
                    processingRegisteredId,
                    processingRegisterStatus
                )
            )
        );
        postsDto = new PostsDto(postListDtos);


        // post
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
}
