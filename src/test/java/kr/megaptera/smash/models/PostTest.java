package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void addHits() {
        Long postId = 1L;
        Post post = Post.fake(postId);
        Long hits = post.hits().value();

        post.addHits();
        post.addHits();
        post.addHits();
        Long hitsAfterSering = post.hits().value();

        assertThat(hitsAfterSering - hits).isEqualTo(3);
    }

    @Test
    void hasNoImages() {
        Long postId = 1L;
        Post post = Post.fake(postId);
        assertThat(post.hasNoImages()).isTrue();
    }

    @Test
    void thumbnailImageUrl() {
        List<PostImage> postImages = List.of(
            PostImage.fakeThumbnailImage(),
            PostImage.fakeNotThumbnailImage(),
            PostImage.fakeNotThumbnailImage()
        );
        Post post = Post.fakeWithImages(postImages);

        assertThat(post.thumbnailImageUrl()).contains("Thumbnail");
    }
}
