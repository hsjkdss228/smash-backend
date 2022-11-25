package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.exceptions.CreatePostFailed;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameDateTime;
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

import java.time.LocalDate;
import java.time.LocalTime;

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
        Long accessedUserId,
        String gameExercise,
        String gameDate,
        String gameTime,
        String gamePlace,
        Integer gameTargetMemberCount,
        String postDetail
    ) {
        User user = userRepository.findById(accessedUserId)
            .orElseThrow(() -> new CreatePostFailed("사용자를 찾을 수 없습니다."));

        Post post = new Post(
            user.id(),
            new PostHits(0L),
            new PostDetail(postDetail)
        );
        Post savedPost = postRepository.save(post);

        GameDateTime gameDateTime = createGameDateTime(
            gameDate,
            gameTime
        );

        Game game = new Game(
            savedPost.id(),
            new Exercise(gameExercise),
            gameDateTime,
            new Place(gamePlace),
            new GameTargetMemberCount(gameTargetMemberCount)
        );
        gameRepository.save(game);

        return new CreatePostAndGameResultDto(savedPost.id());
    }

    public GameDateTime createGameDateTime(String gameDate,
                                            String gameTime) {
        String[] yearMonthDay = gameDate.split("-");
        int year = parseDateType(yearMonthDay[0]);
        int month = parseDateType(yearMonthDay[1]);
        int day = parseDateType(yearMonthDay[2].split("T")[0]);

        String[] startAndEndHourMinute = gameTime.split(",");
        int startHour = Integer.parseInt(startAndEndHourMinute[0]);
        int startMinute = Integer.parseInt(startAndEndHourMinute[1]);
        int endHour = Integer.parseInt(startAndEndHourMinute[2]);
        int endMinute = Integer.parseInt(startAndEndHourMinute[3]);

        return new GameDateTime(
            LocalDate.of(year, month, day),
            LocalTime.of(startHour, startMinute),
            LocalTime.of(endHour, endMinute)
        );
    }

    public int parseDateType(String dateType) {
        return Integer.parseInt(dateType);
    }
}
