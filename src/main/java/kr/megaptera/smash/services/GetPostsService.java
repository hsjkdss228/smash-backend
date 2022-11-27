package kr.megaptera.smash.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RegisterRepository;

@Service
@Transactional
public class GetPostsService {
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final RegisterRepository registerRepository;

    public GetPostsService(PostRepository postRepository,
                           GameRepository gameRepository,
                           RegisterRepository registerRepository) {
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.registerRepository = registerRepository;
    }

    public PostsDto findAll(Long accessedUserId) throws PostsFailed {
        try {
            List<Post> posts = postRepository.findAll();

            List<Game> games = posts.stream()
                    .map(post -> gameRepository.findByPostId(post.id())
                            .orElseThrow(GameNotFound::new))
                    .toList();

            List<Register> members = new ArrayList<>();
            games.forEach(game -> {
                List<Register> membersOfGame
                        = registerRepository.findAllByGameId(game.id())
                        .stream()
                        // member라고 하면 아래에 있는 코드를 읽는 게 불가능합니다.
                        // “Member인데 Register 메서드를 쓴다고?
                        //  Member가 Register를 상속했나?”
                        // 이런 식으로 쓸데 없는 생각을 하게 되죠.
                        // 코드가 (잘못된 이름으로) 거짓말을 하는 전형적인 케이스입니다.
                        .filter(Register::accepted)
                        .toList();

                members.addAll(membersOfGame);
            });

            return createPostDtos(
                    posts,
                    games,
                    members,
                    accessedUserId
            );
        } catch (GameNotFound exception) {
            throw new PostsFailed(exception.getMessage());
        }
    }

    private PostsDto createPostDtos(List<Post> posts,
                                    List<Game> games,
                                    List<Register> members,
                                    Long accessedUserId
    ) {
        List<PostListDto> postListDtos = posts.stream()
                .map(post -> {
                    Boolean isAuthor = post.userId().equals(accessedUserId);

                    Game gameOfPost = games.stream()
                            .filter(game -> game.postId().equals(post.id()))
                            .findFirst().get();

                    Integer currentMemberCount = members.stream()
                            .filter(member -> member.gameId().equals(gameOfPost.id()))
                            .toList()
                            .size();

                    Register myRegister = registerRepository
                            .findAllByGameIdAndUserId(gameOfPost.id(), accessedUserId)
                            .stream()
                            .filter(Register::active)
                            .findFirst().orElse(null);

                    Long registerId = myRegister == null
                            ? -1
                            : myRegister.id();

                    String registerStatus = myRegister == null
                            ? "none"
                            : myRegister.status().toString();

                    GameInPostListDto gameInPostListDto
                            = gameOfPost.toGameInPostListDto(
                            currentMemberCount,
                            registerId,
                            registerStatus
                    );

                    return post.toPostListDto(isAuthor, gameInPostListDto);
                })
                .toList();

        return new PostsDto(postListDtos);
    }
}
