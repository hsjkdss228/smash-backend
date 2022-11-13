package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GetPostsService {
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;

    public GetPostsService(PostRepository postRepository,
                           GameRepository gameRepository,
                           MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
    }

    public PostsDto findAll(Long accessedUserId) throws PostsFailed {
        try {
            List<Post> posts = postRepository.findAll();

            List<Game> games = posts.stream()
                .map(post -> gameRepository.findByPostId(post.id())
                    .orElseThrow(GameNotFound::new))
                .toList();

            List<Member> members = new ArrayList<>();
            games.forEach(game -> {
                List<Member> membersOfGame = memberRepository.findByGameId(game.id());
                members.addAll(membersOfGame);
            });

            return createPostDtos(posts, games, members, accessedUserId);
        } catch (GameNotFound exception) {
            throw new PostsFailed(exception.getMessage());
        }
    }

    private PostsDto createPostDtos(List<Post> posts,
                                    List<Game> games,
                                    List<Member> members,
                                    Long accessedUserId
    ) {
        List<PostListDto> postListDtos = posts.stream()
            .map(post -> {
                Game gameOfPost = games.stream()
                    .filter(game -> game.postId().equals(post.id()))
                    .findFirst().get();

                Integer currentMemberCount = members.stream()
                    .filter(member -> member.gameId().equals(gameOfPost.id()))
                    .toList()
                    .size();

                Boolean isRegistered = members.stream()
                    .anyMatch(member -> member.gameId().equals(gameOfPost.id())
                        && member.userId().equals(accessedUserId));

                GameInPostListDto gameInPostListDto
                    = gameOfPost.toGameInPostListDto(
                    currentMemberCount, isRegistered);

                return post.toPostListDto(gameInPostListDto);
            })
            .toList();

        return new PostsDto(postListDtos);
    }
}
