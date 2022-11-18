package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.models.UserGender;
import kr.megaptera.smash.models.UserName;
import kr.megaptera.smash.models.UserPhoneNumber;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GetPostServiceTest {
    private PostRepository postRepository;
    private UserRepository userRepository;

    private GetPostService getPostService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);
        getPostService = new GetPostService(postRepository, userRepository);
    }

    @Test
    void findTargetPostWhenIsAuthor() {
        Long postId = 1L;
        Long userId = 1L;
        Post post = new Post(
            postId,
            userId,
            new PostHits(1234L),
            new PostDetail("평일 오전 볼링 내기 ㄱ?"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        User user = new User(
            userId,
            new UserName("볼링맨"),
            new UserGender("남성"),
            new UserPhoneNumber("010-8888-8888")
        );
        Long targetPostId = 1L;
        Long accessedUserId = 1L;

        given(postRepository.findById(targetPostId)).willReturn(Optional.of(post));
        given(userRepository.findById(post.id())).willReturn(Optional.of(user));

        PostDetailDto postDetailDto
            = getPostService.findTargetPost(accessedUserId, targetPostId);

        assertThat(postDetailDto).isNotNull();
        assertThat(postDetailDto.getAuthorName()).isEqualTo("볼링맨");
        assertThat(postDetailDto.getIsAuthor()).isEqualTo(true);

        verify(postRepository).findById(targetPostId);
        verify(userRepository).findById(post.id());
    }
}
