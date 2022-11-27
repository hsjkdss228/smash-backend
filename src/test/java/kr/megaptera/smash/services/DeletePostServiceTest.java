package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.DeletePostFailed;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDateTime;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
        post = new Post(
            postId,
            userId,
            new PostHits(144L),
            new PostDetail("압도적 모집 게시글")
        );
        game = new Game(
            gameId,
            postId,
            new Exercise("압도적 운동"),
            new GameDateTime(
                LocalDate.of(2022, 12, 25),
                LocalTime.of(12, 30),
                LocalTime.of(15, 0)
            ),
            new Place("압도적 장소"),
            new GameTargetMemberCount(2000)
        );
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
            .willThrow(DeletePostFailed.class);

        assertThrows(DeletePostFailed.class, () -> {
            deletePostService.deletePost(userId, postId);
        });

        verify(postRepository).findById(postId);
        verify(gameRepository, never()).findByPostId(postId);
        verify(registerRepository, never()).deleteAllByGameId(game.id());
        verify(gameRepository, never()).deleteByPostId(postId);
        verify(postRepository, never()).deleteById(postId);
    }

    @Test
    void deletePostWithNotAuthor() {
        Long userIdNotAuthor = 2L;
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));

        assertThrows(DeletePostFailed.class, () -> {
            deletePostService.deletePost(userIdNotAuthor, userId);
        });

        verify(postRepository).findById(postId);
        verify(gameRepository, never()).findByPostId(postId);
        verify(registerRepository, never()).deleteAllByGameId(game.id());
        verify(gameRepository, never()).deleteByPostId(postId);
        verify(postRepository, never()).deleteById(postId);
    }

    @Test
    void deletePostWithGameNotFound() {
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        given(gameRepository.findByPostId(postId))
            .willThrow(DeletePostFailed.class);

        assertThrows(DeletePostFailed.class, () -> {
            deletePostService.deletePost(userId, postId);
        });

        verify(postRepository).findById(postId);
        verify(gameRepository).findByPostId(postId);
        verify(registerRepository, never()).deleteAllByGameId(game.id());
        verify(gameRepository, never()).deleteByPostId(postId);
        verify(postRepository, never()).deleteById(postId);
    }
}
