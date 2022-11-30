package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePostService {
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public CreatePostService(PostRepository postRepository,
                             GameRepository gameRepository,
                             UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public CreatePostAndGameResultDto createPost(
        Long currentUserId,
        String gameExercise,
        String gameDate,
        String gameStartTimeAmPm,
        String gameStartHour,
        String gameStartMinute,
        String gameEndTimeAmPm,
        String gameEndHour,
        String gameEndMinute,
        String gamePlace,
        Integer gameTargetMemberCount,
        String postDetail
    ) {
        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFound(currentUserId));

        Post post = new Post(
            user.id(),
            new PostHits(0L),
            new PostDetail(postDetail)
        );
        Post savedPost = postRepository.save(post);

        Game game = new Game(
            savedPost.id(),
            new Exercise(gameExercise),
            new Place(gamePlace),
            new GameTargetMemberCount(gameTargetMemberCount)
        );

        game.createGameDateTime(
            gameDate,
            gameStartTimeAmPm,
            gameStartHour,
            gameStartMinute,
            gameEndTimeAmPm,
            gameEndHour,
            gameEndMinute
        );

        gameRepository.save(game);

        return new CreatePostAndGameResultDto(savedPost.id());
    }
}
