package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Post;
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

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPostsService getPostsService;

    private List<PostListDto> postListDtos;

    @SpyBean
    private JwtUtil jwtUtil;

    private Long userId = 1L;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtUtil.encode(userId);

        long generationCount = 2;
        List<Post> posts = Post.fakes(generationCount);
        List<Game> games = Game.fakes(generationCount);
        List<List<Member>> membersOfGames = new ArrayList<>();
        for (long gameId = 1; gameId <= generationCount; gameId += 1) {
            List<Member> members = Member.fakes(generationCount, gameId);
            membersOfGames.add(members);
        }

        // TODO: true, false를 어떻게 의미있는 값으로 반환?

        postListDtos = List.of(
            new PostListDto(
                posts.get(0).id(),
                posts.get(0).hits().value(),
                new GameInPostListDto(
                    games.get(0).id(),
                    games.get(0).exercise().name(),
                    games.get(0).date().value(),
                    games.get(0).place().name(),
                    membersOfGames.get(0).size(),
                    games.get(0).targetMemberCount().value(),
                    true
                )
            ),
            new PostListDto(
                posts.get(1).id(),
                posts.get(1).hits().value(),
                new GameInPostListDto(
                    games.get(1).id(),
                    games.get(1).exercise().name(),
                    games.get(1).date().value(),
                    games.get(1).place().name(),
                    membersOfGames.get(1).size(),
                    games.get(1).targetMemberCount().value(),
                    false
                )
            )
        );
    }

    @Test
    void posts() throws Exception {
        PostsDto postsDto = new PostsDto(postListDtos);
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
                containsString("\"isRegistered\":false")
            ))
        ;
    }
}
