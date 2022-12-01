package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {
    @Test
    void isAuthor() {
        Long postId = 1L;
        Long userId = 1L;
        Post post = Post.fake(postId);
        User user = User.fake(userId);
        assertThat(post.isAuthor(user)).isTrue();
    }

    @Test
    void isNotAuthor() {
        Long postId = 1L;
        Long userId = 2L;
        Post post = Post.fake(postId);
        User user = User.fake(userId);
        assertThat(post.isAuthor(user)).isFalse();
    }

    @Test
    void isNotAuthorWithNull() {
        Long postId = 1L;
        Post post = Post.fake(postId);
        User user = null;
        assertThat(post.isAuthor(user)).isFalse();
    }
}
