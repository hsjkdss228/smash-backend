package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.PostNotFound;
import kr.megaptera.smash.exceptions.UserIsNotAuthor;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeletePostService {
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final RegisterRepository registerRepository;

    public DeletePostService(PostRepository postRepository,
                             GameRepository gameRepository,
                             RegisterRepository registerRepository) {
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.registerRepository = registerRepository;
    }

    public void deletePost(Long accessedUserId, Long targetPostId) {
        Post post = postRepository.findById(targetPostId)
            .orElseThrow(() -> new PostNotFound(targetPostId));

        if (!post.userId().equals(accessedUserId)) {
            throw new UserIsNotAuthor();
        }

        Game game = gameRepository.findByPostId(post.id())
            .orElseThrow(GameNotFound::new);

        registerRepository.deleteAllByGameId(game.id());

        gameRepository.deleteByPostId(post.id());

        postRepository.deleteById(post.id());
    }
}
