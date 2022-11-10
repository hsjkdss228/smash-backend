package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.services.GetPostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    private Post post1;
    private Game gameOfPost1;
    private List<Member> membersOfGame1;

    private Post post2;
    private Game gameOfPost2;
    private List<Member> membersOfGame2;

    @BeforeEach
    void setUp() {
        post1 = Post.fake(1L);
        post2 = Post.fake(2L);
        gameOfPost1 = Game.fake(1L, 1L);
        gameOfPost2 = Game.fake(2L, 2L);
        membersOfGame1 = List.of(
            Member.fake(1L, 1L),
            Member.fake(2L, 1L)
        );
        membersOfGame2 = List.of(
            Member.fake(3L, 2L)
        );
    }

    @Test
    void posts() throws Exception {
        List<PostListDto> postListDtos = List.of(
            new PostListDto(
                post1.id(),
                post1.hits(),
                new GameInPostListDto(
                    gameOfPost1.type(),
                    gameOfPost1.date(),
                    gameOfPost1.place(),
                    membersOfGame1.size(),
                    gameOfPost1.targetMemberCount()
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
                    gameOfPost2.targetMemberCount()
                )
            )
        );
        PostsDto postsDto = new PostsDto(postListDtos);
        given(getPostsService.findAll()).willReturn(postsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"hits\":100")
            ))
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"currentMemberCount\":1")
            ));
    }
}
