package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.PostNotFound;
import kr.megaptera.smash.exceptions.UserIsNotAuthor;
import kr.megaptera.smash.models.game.Game;
import kr.megaptera.smash.models.post.Post;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DeletePostServiceTest {
    private DeletePostService deletePostService;

    private PostRepository postRepository;
    private GameRepository gameRepository;
    private RegisterRepository registerRepository;

    private Long postId;
    private Long userId;
    private Long gameId;
    private Post post;
    private Game game;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        gameRepository = mock(GameRepository.class);
        registerRepository = mock(RegisterRepository.class);

        deletePostService = new DeletePostService(
            postRepository,
            gameRepository,
            registerRepository
        );

        postId = 1L;
        userId = 1L;
        gameId = 1L;
        post = Post.fake(postId);
        game = Game.fake(gameId);
    }

    @Test
    void deletePost() {
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        given(gameRepository.findByPostId(postId))
            .willReturn(Optional.of(game));

        deletePostService.deletePost(userId, postId);

        verify(postRepository).findById(postId);
        verify(gameRepository).findByPostId(postId);
        verify(registerRepository).deleteAllByGameId(game.id());
        verify(gameRepository).deleteByPostId(postId);
        verify(postRepository).deleteById(postId);
    }

    @Test
    void deletePostWithPostNotFound() {
        given(postRepository.findById(postId))
            .willThrow(PostNotFound.class);

        assertThrows(PostNotFound.class, () -> {
            deletePostService.deletePost(userId, postId);
        });
    }

    @Test
    void deletePostWithNotAuthor() {
        Long userIdNotAuthor = 2L;
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));

        assertThrows(UserIsNotAuthor.class, () -> {
            deletePostService.deletePost(userIdNotAuthor, userId);
        });
    }

    @Test
    void deletePostWithGameNotFound() {
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        given(gameRepository.findByPostId(postId))
            .willThrow(GameNotFound.class);

        assertThrows(GameNotFound.class, () -> {
            deletePostService.deletePost(userId, postId);
        });
    }
}
