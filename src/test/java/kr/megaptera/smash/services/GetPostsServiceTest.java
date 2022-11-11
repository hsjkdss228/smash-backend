package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetPostsServiceTest {
    private GetPostsService getPostsService;

    private PostRepository postRepository;
    private GameRepository gameRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        gameRepository = mock(GameRepository.class);
        memberRepository = mock(MemberRepository.class);
        getPostsService = new GetPostsService(
            postRepository,
            gameRepository,
            memberRepository
        );
    }

    @Test
    void posts() {
        List<Post> posts = List.of(
            Post.fake(1L),
            Post.fake(2L)
        );
        Game gameOfPost1 = Game.fake(1L, 1L);
        Game gameOfPost2 = Game.fake(2L, 2L);
        List<Member> membersOfGame1 = List.of(
            Member.fake(1L, 1L, 1L),
            Member.fake(2L, 2L, 1L)
        );
        List<Member> membersOfGame2 = List.of(
            Member.fake(3L, 3L, 2L)
        );

        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(1L)).willReturn(Optional.of(gameOfPost1));
        given(gameRepository.findByPostId(2L)).willReturn(Optional.of(gameOfPost2));
        given(memberRepository.findByGameId(1L)).willReturn(membersOfGame1);
        given(memberRepository.findByGameId(2L)).willReturn(membersOfGame2);

        Long accessedUserId = 1L;
        PostsDto postsDto = getPostsService.findAll(accessedUserId);

        assertThat(postsDto).isNotNull();

        List<PostListDto> postListDtos = postsDto.getPosts();
        assertThat(postListDtos.get(0).getId()).isEqualTo(1L);
        assertThat(postListDtos.get(0).getGame().getType()).isEqualTo("운동 종류");
        assertThat(postListDtos.get(0).getGame().getCurrentMemberCount()).isEqualTo(2);
        assertThat(postListDtos.get(0).getGame().getIsRegistered()).isEqualTo(true);
        assertThat(postListDtos.get(1).getHits()).isEqualTo(100L);
        assertThat(postListDtos.get(1).getGame().getPlace()).isEqualTo("운동 장소");
        assertThat(postListDtos.get(1).getGame().getCurrentMemberCount()).isEqualTo(1);
        assertThat(postListDtos.get(1).getGame().getIsRegistered()).isEqualTo(false);
    }
}
