package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.GameDto;
import kr.megaptera.smash.dtos.ImageDto;
import kr.megaptera.smash.dtos.MemberDto;
import kr.megaptera.smash.dtos.PostDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.dtos.RoleDto;
import kr.megaptera.smash.dtos.TeamDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.PostNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Game;
import kr.megaptera.smash.models.Image;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Role;
import kr.megaptera.smash.models.Team;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.GameRepository;
import kr.megaptera.smash.repositories.ImageRepository;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.PlaceRepository;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RoleRepository;
import kr.megaptera.smash.repositories.TeamRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PostService {
  private static final String IS_AUTHOR = "isAuthor";
  private static final String IS_REGISTERED = "isRegistered";
  private static final String IS_NOT_REGISTERED = "isNotRegistered";

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ImageRepository imageRepository;
  private final TeamRepository teamRepository;
  private final RoleRepository roleRepository;
  private final MemberRepository memberRepository;
  private final PlaceRepository placeRepository;
  private final GameRepository gameRepository;

  public PostService(PostRepository postRepository,
                     UserRepository userRepository,
                     ImageRepository imageRepository,
                     GameRepository gameRepository,
                     TeamRepository teamRepository,
                     RoleRepository roleRepository,
                     MemberRepository memberRepository,
                     PlaceRepository placeRepository
  ) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.imageRepository = imageRepository;
    this.gameRepository = gameRepository;
    this.teamRepository = teamRepository;
    this.roleRepository = roleRepository;
    this.memberRepository = memberRepository;
    this.placeRepository = placeRepository;
  }

  public PostsDto posts() {
    List<Post> posts = postRepository.findAll();

    List<PostDto> postDtos = posts.stream()
        .map(this::createPostDto)
        .toList();

    return new PostsDto(postDtos);
  }

  public PostDto post(Long postId, Long accessedUserId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    return createPostDto(post, accessedUserId);
  }

  public PostDto createPostDto(Post post) {
    Game game = gameRepository.findByPostId(post.id())
        .orElseThrow(GameNotFound::new);
    List<Team> teams = teamRepository.findAllByGameId(game.id());
    List<Role> roles = roleRepository.findAllByGameId(game.id());
    List<Member> members = memberRepository.findAllByGameId(game.id());

    List<MemberDto> memberDtos = createMemberDtos(members);
    List<RoleDto> roleDtos = createRoleDtos(roles, memberDtos);
    List<TeamDto> teamDtos = createTeamDtos(teams, roleDtos);

    Place place = placeRepository.findByGameId(game.id());

    GameDto gameDto = game.toDto(place.name(), teamDtos);

    User author = userRepository.findById(post.id())
        .orElseThrow(UserNotFound::new);

    List<ImageDto> imageDtos = imageRepository.findAllByPostId(post.id())
        .stream()
        .map(Image::toDto)
        .toList();

    return post.toPostDto(author.name(), imageDtos, gameDto);
  }

  public PostDto createPostDto(Post post, Long accessedUserId) {
    Game game = gameRepository.findByPostId(post.id())
        .orElseThrow(GameNotFound::new);
    List<Team> teams = teamRepository.findAllByGameId(game.id());
    List<Role> roles = roleRepository.findAllByGameId(game.id());
    List<Member> members = memberRepository.findAllByGameId(game.id());

    List<MemberDto> memberDtos = createMemberDtos(members);
    List<RoleDto> roleDtos = createRoleDtos(roles, memberDtos);
    List<TeamDto> teamDtos = createTeamDtos(teams, roleDtos);

    Place place = placeRepository.findByGameId(game.id());

    String userStatus = IS_NOT_REGISTERED;
    Long roleIdOfAccessedUser = 0L;

    Map<String, Long> statusAndRoleId = members.stream()
        .filter(member -> member.userId().equals(accessedUserId))
        .map(found -> Map.of(IS_REGISTERED, found.roleId()))
        .findFirst()
        .orElse(null);
    if (!(statusAndRoleId == null)) {
      userStatus = statusAndRoleId.keySet().stream().findFirst().get();
      roleIdOfAccessedUser = statusAndRoleId.get(IS_REGISTERED);
    }
    if (accessedUserId.equals(post.authorId())) {
      userStatus = IS_AUTHOR;
      roleIdOfAccessedUser = 0L;
    }

    GameDto gameDto = game.toDto(
        place.name(),
        teamDtos,
        userStatus,
        roleIdOfAccessedUser
    );

    User author = userRepository.findById(post.id())
        .orElseThrow(UserNotFound::new);

    List<ImageDto> imageDtos = imageRepository.findAllByPostId(post.id())
        .stream()
        .map(Image::toDto)
        .toList();

    return post.toPostDto(author.name(), imageDtos, gameDto);
  }

  public List<MemberDto> createMemberDtos(List<Member> members) {
    return members.stream()
        .map(this::createMemberDto)
        .toList();
  }

  public MemberDto createMemberDto(Member member) {
    User user = userRepository.findById(member.userId())
        .orElseThrow(UserNotFound::new);

    return member.toMemberDto(user.name(), user.mannerScore());
  }

  private List<RoleDto> createRoleDtos(List<Role> roles, List<MemberDto> memberDtos) {
    return roles.stream()
        .map(role -> createRoleDto(memberDtos, role))
        .toList();
  }

  public RoleDto createRoleDto(List<MemberDto> memberDtos, Role role) {
    List<MemberDto> memberDtosInRole = memberDtos.stream()
        .filter(memberDto -> memberDto.getRoleId().equals(role.id()))
        .toList();

    return role.toRoleDto(memberDtosInRole.size(), memberDtosInRole);
  }

  private List<TeamDto> createTeamDtos(List<Team> teams, List<RoleDto> roleDtos) {
    return teams.stream()
        .map(team -> createTeamDto(roleDtos, team))
        .toList();
  }

  public TeamDto createTeamDto(List<RoleDto> roleDtos, Team team) {
    List<RoleDto> roleDtosInTeam = roleDtos.stream()
        .filter(roleDto -> roleDto.getTeamId().equals(team.id()))
        .toList();

    Integer membersCount = roleDtosInTeam.stream()
        .map(RoleDto::getCurrentParticipants)
        .reduce(0, Integer::sum);

    return team.toTeamDto(membersCount, roleDtosInTeam);
  }
}
