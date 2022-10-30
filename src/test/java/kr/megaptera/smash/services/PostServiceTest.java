package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostThumbnailsDto;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Role;
import kr.megaptera.smash.models.Team;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RoleRepository;
import kr.megaptera.smash.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostServiceTest {
  private PostService postService;
  private PostRepository postRepository;
  private TeamRepository teamRepository;
  private RoleRepository roleRepository;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    teamRepository = mock(TeamRepository.class);
    roleRepository = mock(RoleRepository.class);
    postService = new PostService(
        postRepository,
        teamRepository,
        roleRepository
    );
  }

  @Test
  void posts() {
    given(postRepository.findAll())
        .willReturn(List.of(
            // id, author, detail
            new Post(1L, new User("작성자 1"), "동네 야구대회 나가실 분 모집합니다"),
            new Post(3L, new User("작성자 2"), "풋살마렵네 재야의 고수들 모여라")
        ));
    given(teamRepository.findAll())
        .willReturn(List.of(
            // id, postIn, name, membersUnder, targetMembersCount
            new Team(
                1L,
                new Post(1L),
                List.of(new Member(), new Member(), new Member(), new Member()),
                12
            ),
            new Team(
                2L,
                new Post(3L),
                List.of(new Member(), new Member(), new Member(), new Member(), new Member()),
                6
            )
        ));
    given(roleRepository.findAll())
        .willReturn(List.of(
            // id, teamIn, name, targetParticipantsCount
            new Role(
                1L,
                new Team(1L),
                "투수",
                3,
                List.of()
            ),
            new Role(
                2L,
                new Team(1L),
                "내야수",
                5,
                List.of(new Member(), new Member())
            ),
            new Role(
                3L,
                new Team(1L),
                "외야수",
                4,
                List.of(new Member(), new Member())
            ),
            new Role(
                4L,
                new Team(2L),
                "자유포지션",
                6,
                List.of(new Member(), new Member(), new Member(), new Member(), new Member())
            )
        ));

    PostThumbnailsDto postThumbnailsDto = postService.posts();

    verify(postRepository).findAll();
    verify(teamRepository).findAll();
    verify(roleRepository).findAll();
  }
}
