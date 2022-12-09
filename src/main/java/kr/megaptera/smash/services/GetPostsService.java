package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PlaceInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.PlaceNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetPostsService {
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final PlaceRepository placeRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public GetPostsService(UserRepository userRepository,
                           PostRepository postRepository,
                           GameRepository gameRepository,
                           PlaceRepository placeRepository,
                           RegisterRepository registerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.placeRepository = placeRepository;
        this.registerRepository = registerRepository;
    }

    public PostsDto findAll(Long currentUserId) {
        User currentUser = currentUserId == null
            ? null
            : userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFound(currentUserId));

        List<Post> Posts = postRepository.findAll();

        List<PostListDto> postListDtos = Posts.stream()
            .map(post -> {
                Game game = gameRepository.findByPostId(post.id())
                    .orElseThrow(GameNotFound::new);

                Place place = placeRepository.findById(game.placeId())
                    .orElseThrow(() -> new PlaceNotFound(game.placeId()));

                List<Register> registers
                    = registerRepository.findAllByGameId(game.id());

                Boolean isAuthor = post.isAuthor(currentUser);

                Integer currentMemberCount = game.countCurrentMembers(registers);

                Register myRegister = game.findMyRegister(currentUser, registers);

                Long registerId = myRegister == null
                    ? null
                    : myRegister.id();

                String registerStatus = myRegister == null
                    ? "none"
                    : myRegister.status().toString();

                GameInPostListDto gameInPostListDto
                    = game.toGameInPostListDto(
                    currentMemberCount,
                    registerId,
                    registerStatus
                );

                PlaceInPostListDto placeInPostListDto
                    = place.toPlaceInPostListDto();

                return post.toPostListDto(
                    isAuthor,
                    gameInPostListDto,
                    placeInPostListDto
                );
            })
            .toList();

        return new PostsDto(postListDtos);
    }
}
