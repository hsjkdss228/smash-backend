package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.DeletePostFailed;
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
            .orElseThrow(() -> new DeletePostFailed(
                "삭제하려는 게시물 번호에 해당하는 게시물을 찾을 수 없습니다."));

        if (!post.userId().equals(accessedUserId)) {
            throw new DeletePostFailed("접속한 사용자가 게시글 작성자가 아닙니다.");
        }

        Game game = gameRepository.findByPostId(post.id())
            .orElseThrow(() -> new DeletePostFailed(
                "삭제하려는 게시물에 연결된 경기 정보를 찾을 수 없습니다."));

        registerRepository.deleteAllByGameId(game.id());

        gameRepository.deleteByPostId(post.id());

        postRepository.deleteById(post.id());
    }
}
