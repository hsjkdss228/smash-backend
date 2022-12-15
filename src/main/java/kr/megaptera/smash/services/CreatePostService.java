package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.ExerciseForPostCreateRequestDto;
import kr.megaptera.smash.dtos.GameForPostCreateRequestDto;
import kr.megaptera.smash.dtos.PlaceForPostCreateRequestDto;
import kr.megaptera.smash.dtos.PostForPostCreateRequestDto;
import kr.megaptera.smash.exceptions.PlaceNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Exercise;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.GameTargetMemberCount;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.PostDetail;
import kr.megaptera.smash.models.PostHits;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePostService {
    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final RegisterRepository registerRepository;

    public CreatePostService(PostRepository postRepository,
                             PlaceRepository placeRepository,
                             GameRepository gameRepository,
                             UserRepository userRepository,
                             RegisterRepository registerRepository) {
        this.postRepository = postRepository;
        this.placeRepository = placeRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.registerRepository = registerRepository;
    }

    public CreatePostAndGameResultDto createPost(
        Long currentUserId,
        PostForPostCreateRequestDto postForPostCreateRequestDto,
        GameForPostCreateRequestDto gameForPostCreateRequestDto,
        ExerciseForPostCreateRequestDto exerciseForPostCreateRequestDto,
        PlaceForPostCreateRequestDto placeForPostCreateRequestDto
    ) {
        User user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFound(currentUserId));

        String detail = postForPostCreateRequestDto.getDetail();

        Post post = new Post(
            user.id(),
            new PostHits(0L),
            new PostDetail(detail)
        );

        Post savedPost = postRepository.save(post);

        String placeName = placeForPostCreateRequestDto.getName();

        Place place = placeRepository.findByInformationName(placeName)
            .orElseThrow(() -> new PlaceNotFound(placeName));

        String exerciseName = exerciseForPostCreateRequestDto.getName();

        Integer gameTargetMemberCount
            = gameForPostCreateRequestDto.getTargetMemberCount();

        // TODO: 장소 입력 시 카테고리를 구분하지 않기 때문에
        //   똑같은 이름의 장소가 지금은 존재할 수가 없다.
        //   findByAnd 형테를 갖추기 위해서는 운동 카테고리도 입력받게 해야 한다.

        Game game = new Game(
            savedPost.id(),
            place.id(),
            new Exercise(exerciseName),
            new GameTargetMemberCount(gameTargetMemberCount)
        );

        String gameDate = gameForPostCreateRequestDto.getDate();
        String gameStartTimeAmPm = gameForPostCreateRequestDto.getStartTimeAmPm();
        String gameStartHour = gameForPostCreateRequestDto.getStartHour();
        String gameStartMinute = gameForPostCreateRequestDto.getStartMinute();
        String gameEndTimeAmPm = gameForPostCreateRequestDto.getEndTimeAmPm();
        String gameEndHour = gameForPostCreateRequestDto.getEndHour();
        String gameEndMinute = gameForPostCreateRequestDto.getEndMinute();

        game.createGameDateTime(
            gameDate,
            gameStartTimeAmPm,
            gameStartHour,
            gameStartMinute,
            gameEndTimeAmPm,
            gameEndHour,
            gameEndMinute
        );

        Game savedGame = gameRepository.save(game);

        Register register = savedGame.join(user);

        registerRepository.save(register);

        return new CreatePostAndGameResultDto(savedPost.id());
    }
}
