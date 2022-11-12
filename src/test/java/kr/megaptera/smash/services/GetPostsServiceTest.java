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

import java.util.ArrayList;
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
        long generationCount = 2;
        List<Post> posts = Post.fakes(generationCount);
        List<Game> games = Game.fakes(generationCount);
        List<List<Member>> membersOfGames = new ArrayList<>();
        for (long gameId = 1; gameId <= generationCount; gameId += 1) {
            List<Member> members = Member.fakes(generationCount, gameId);
            membersOfGames.add(members);
        }

        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(1L)).willReturn(Optional.of(games.get(0)));
        given(gameRepository.findByPostId(2L)).willReturn(Optional.of(games.get(1)));
        given(memberRepository.findByGameId(1L)).willReturn(membersOfGames.get(0));
        given(memberRepository.findByGameId(2L)).willReturn(membersOfGames.get(1));

        Long accessedUserId = 1L;
        PostsDto postsDto = getPostsService.findAll(accessedUserId);

        assertThat(postsDto).isNotNull();

        List<PostListDto> postListDtos = postsDto.getPosts();
        assertThat(postListDtos.get(0).getId()).isEqualTo(1L);
        assertThat(postListDtos.get(0).getGame().getType()).isEqualTo("운동 종류");
        assertThat(postListDtos.get(0).getGame().getCurrentMemberCount()).isEqualTo(2);
        assertThat(postListDtos.get(0).getGame().getIsRegistered()).isEqualTo(true);
        assertThat(postListDtos.get(1).getHits()).isEqualTo(123L);
        assertThat(postListDtos.get(1).getGame().getPlace()).isEqualTo("운동 장소");
        assertThat(postListDtos.get(1).getGame().getCurrentMemberCount()).isEqualTo(2);
        assertThat(postListDtos.get(1).getGame().getIsRegistered()).isEqualTo(true);
    }
}
