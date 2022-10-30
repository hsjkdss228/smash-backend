package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PostThumbnailDto;
import kr.megaptera.smash.dtos.PostThumbnailsDto;
import kr.megaptera.smash.dtos.RoleDto;
import kr.megaptera.smash.dtos.TeamDto;
import kr.megaptera.smash.services.PostService;
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
  private PostService postService;

  @Test
  void posts() throws Exception {
    PostThumbnailsDto postThumbnailsDto = new PostThumbnailsDto(
        List.of(
            // id, author, detail
            new PostThumbnailDto(1L, "작성자 1", "Local Baseball Family"),
            new PostThumbnailDto(3L, "작성자 2", "Soccer Gogo")
        ),
        List.of(
            // id, postId, name, membersCount, targetMembersCount
            new TeamDto(1L, 1L, "단일 팀", 4, 12),
            new TeamDto(2L, 3L, "단일 팀", 5, 6)
        ),
        List.of(
            // id, teamId, name, currentParticipants, targetParticipantsCount
            new RoleDto(1L, 1L, "투수", 0, 3),
            new RoleDto(2L, 1L, "내야수", 2, 5),
            new RoleDto(3L, 1L, "외야수", 2, 4),
            new RoleDto(4L, 2L, "자유포지션", 5, 6)
        )
    );
    given(postService.posts()).willReturn(postThumbnailsDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/posts/list"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(
            containsString("Local Baseball Family")
        ));
  }
}
