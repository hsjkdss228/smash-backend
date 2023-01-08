package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.models.game.Game;
import kr.megaptera.smash.models.game.GameTargetMemberCount;
import kr.megaptera.smash.models.place.Place;
import kr.megaptera.smash.models.post.Post;
import kr.megaptera.smash.models.register.Register;
import kr.megaptera.smash.models.user.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetPostsServiceTest {
    private GetPostsService getPostsService;

    private UserRepository userRepository;
    private PostRepository postRepository;
    private GameRepository gameRepository;
    private PlaceRepository placeRepository;
    private RegisterRepository registerRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        postRepository = mock(PostRepository.class);
        gameRepository = mock(GameRepository.class);
        placeRepository = mock(PlaceRepository.class);
        registerRepository = mock(RegisterRepository.class);
        getPostsService = new GetPostsService(
            userRepository,
            postRepository,
            gameRepository,
            placeRepository,
            registerRepository);
    }

    // TODO: id를 값 객체로 변환하면 games.get(0).id() 이런 식으로 주는 것들을
    //   값 객체로 주도록 수정해야 함

    @Test
    void posts() {
        int generationCount = 2;
        List<Post> posts = Post.fakes(generationCount);
        List<Game> games = Game.fakes(
            generationCount,
            new GameTargetMemberCount(3)
        );
        List<Place> places = Place.fakes(generationCount);
        List<Register> registersGame1 = List.of(
            Register.fakeAccepted(1L, games.get(0).id()),
            Register.fakeProcessing(2L, games.get(0).id()),
            Register.fakeRejected(3L, games.get(0).id()),
            Register.fakeCanceled(4L, games.get(0).id())
        );
        List<Register> registersGame2 = List.of(
            Register.fakeAccepted(5L, games.get(1).id()),
            Register.fakeAccepted(6L, games.get(1).id()),
            Register.fakeAccepted(7L, games.get(1).id())
        );
        Long currentUserId = 1L;
        User user = User.fake(currentUserId);

        given(userRepository.findById(currentUserId))
            .willReturn(Optional.of(user));
        given(postRepository.findAll()).willReturn(posts);
        given(gameRepository.findByPostId(posts.get(0).id()))
            .willReturn(Optional.of(games.get(0)));
        given(gameRepository.findByPostId(posts.get(1).id()))
            .willReturn(Optional.of(games.get(1)));
        given(placeRepository.findById(games.get(0).placeId()))
            .willReturn(Optional.of(places.get(0)));
        given(placeRepository.findById(games.get(1).placeId()))
            .willReturn(Optional.of(places.get(1)));
        given(registerRepository.findAllByGameId(games.get(0).id()))
            .willReturn(registersGame1);
        given(registerRepository.findAllByGameId(games.get(1).id()))
            .willReturn(registersGame2);

        PostsDto postsDto = getPostsService.findAll(currentUserId);

        assertThat(postsDto).isNotNull();
        List<PostListDto> postListDtos = postsDto.getPosts();
        assertThat(postListDtos.get(0).getId()).isEqualTo(1L);
        assertThat(postListDtos.get(0).getGame().getType()).isEqualTo("운동 종류 1");
        assertThat(postListDtos.get(0).getGame().getCurrentMemberCount()).isEqualTo(1);
        assertThat(postListDtos.get(0).getGame().getRegisterStatus()).isEqualTo("accepted");
        assertThat(postListDtos.get(1).getPlace().getName()).isEqualTo("운동 장소 이름 2");
        assertThat(postListDtos.get(1).getGame().getCurrentMemberCount()).isEqualTo(3);
        assertThat(postListDtos.get(1).getGame().getRegisterStatus()).isEqualTo("none");
    }
}
