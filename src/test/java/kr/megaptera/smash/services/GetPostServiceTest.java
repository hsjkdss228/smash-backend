package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.models.post.Post;
import kr.megaptera.smash.models.user.User;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
        Post post = Post.fake(postId, userId);
        User user = User.fake(userId);

        Long targetPostId = 1L;
        Long currentUserId = 1L;

        given(postRepository.findById(targetPostId))
            .willReturn(Optional.of(post));
        given(userRepository.findById(currentUserId))
            .willReturn(Optional.of(user));
        given(userRepository.findById(post.userId()))
            .willReturn(Optional.of(user));

        PostDetailDto postDetailDto
            = getPostService.findTargetPost(currentUserId, targetPostId);

        assertThat(postDetailDto).isNotNull();
        assertThat(postDetailDto.getAuthorInformation().getName()).isEqualTo("사용자명");
        assertThat(postDetailDto.getIsAuthor()).isTrue();

        verify(postRepository).findById(targetPostId);
        verify(userRepository, times(2)).findById(currentUserId);
    }

    @Test
    void findTargetPostWhenIsNotAuthor() {
        Long postId = 1L;
        Long userId = 1L;
        Post post = Post.fake(postId, userId);
        User postAuthor = User.fake(userId);

        Long targetPostId = 1L;
        Long currentUserId = 222L;
        User user = User.fake(currentUserId);

        given(postRepository.findById(targetPostId))
            .willReturn(Optional.of(post));
        given(userRepository.findById(currentUserId))
            .willReturn(Optional.of(user));
        given(userRepository.findById(post.userId()))
            .willReturn(Optional.of(postAuthor));

        PostDetailDto postDetailDto
            = getPostService.findTargetPost(currentUserId, targetPostId);

        assertThat(postDetailDto).isNotNull();
        assertThat(postDetailDto.getIsAuthor()).isFalse();

        verify(postRepository).findById(targetPostId);
        verify(userRepository).findById(currentUserId);
        verify(userRepository).findById(post.id());
    }

    @Test
    void findTargetPostWithNotLoggedIn() {
        Long postId = 1L;
        Long userId = 1L;
        Post post = Post.fake(postId, userId);
        User postAuthor = User.fake(userId);

        Long targetPostId = 1L;
        Long currentUserId = null;

        given(postRepository.findById(targetPostId))
            .willReturn(Optional.of(post));
        given(userRepository.findById(post.userId()))
            .willReturn(Optional.of(postAuthor));

        PostDetailDto postDetailDto
            = getPostService.findTargetPost(currentUserId, targetPostId);

        assertThat(postDetailDto).isNotNull();
        assertThat(postDetailDto.getIsAuthor()).isFalse();

        verify(postRepository).findById(targetPostId);
        verify(userRepository).findById(post.id());
    }
}
