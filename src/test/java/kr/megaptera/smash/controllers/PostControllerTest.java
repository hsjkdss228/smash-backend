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

        Post post1 = Post.fake(1L);
        Game gameOfPost1 = Game.fake(1L, 1L);
        List<Member> membersOfGame1 = List.of(
            Member.fake(1L, 1L, 1L),
            Member.fake(2L, 2L, 1L)
        );
        Post post2 = Post.fake(2L);
        Game gameOfPost2 = Game.fake(2L, 2L);
        List<Member> membersOfGame2 = List.of(
            Member.fake(3L, 3L, 2L)
        );

        // TODO: true, false를 어떻게 의미있는 값으로 반환?

        postListDtos = List.of(
            new PostListDto(
                post1.id(),
                post1.hits(),
                new GameInPostListDto(
                    gameOfPost1.type(),
                    gameOfPost1.date(),
                    gameOfPost1.place(),
                    membersOfGame1.size(),
                    gameOfPost1.targetMemberCount(),
                    true
                )
            ),
            new PostListDto(
                post2.id(),
                post2.hits(),
                new GameInPostListDto(
                    gameOfPost2.type(),
                    gameOfPost2.date(),
                    gameOfPost2.place(),
                    membersOfGame2.size(),
                    gameOfPost2.targetMemberCount(),
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
                containsString("\"hits\":100")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"currentMemberCount\":1")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"isRegistered\":false")
            ));
    }
}
