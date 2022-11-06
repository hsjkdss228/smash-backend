package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.MemberDto;
import kr.megaptera.smash.dtos.RoleDto;
import kr.megaptera.smash.dtos.TeamDto;
import kr.megaptera.smash.models.Member;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PostServiceTest {
  private PostService postService;
  private PostRepository postRepository;
  private UserRepository userRepository;
  private ImageRepository imageRepository;
  private GameRepository gameRepository;
  private TeamRepository teamRepository;
  private RoleRepository roleRepository;
  private MemberRepository memberRepository;
  private PlaceRepository placeRepository;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    userRepository = mock(UserRepository.class);
    imageRepository = mock(ImageRepository.class);
    gameRepository = mock(GameRepository.class);
    teamRepository = mock(TeamRepository.class);
    roleRepository = mock(RoleRepository.class);
    memberRepository = mock(MemberRepository.class);
    placeRepository = mock(PlaceRepository.class);
    postService = new PostService(
        postRepository,
        userRepository,
        imageRepository,
        gameRepository,
        teamRepository,
        roleRepository,
        memberRepository,
        placeRepository
    );
  }

//  @Test
//  void createPostDto() {
//    Long gameId = 1L;
//    Long team1Id = 1L;
//    Long role1Id = 1L;
//    Long role2Id = 2L;
//    Long team2Id = 1L;
//    Long role3Id = 3L;
//    Long authorId = 1L;
//    List<Member> members = List.of(
//        new Member(1L, gameId, team1Id, role1Id, authorId),
//        new Member(2L, gameId, team1Id, role1Id, 2L),
//        new Member(3L, gameId, team1Id, role2Id, 3L),
//        new Member(4L, gameId, team2Id, role3Id, 4L)
//    );
//    Double mannerScore = 0.0;
//    List<MemberDto> createdMemberDtos = List.of(
//        new MemberDto(1L, team1Id, role1Id, "황인우", mannerScore),
//        new MemberDto(2L, team1Id, role1Id, "참가자 1", mannerScore),
//        new MemberDto(3L, team1Id, role2Id, "참가자 2", mannerScore),
//        new MemberDto(4L, team2Id, role3Id, "참가자 3", mannerScore)
//    );
//    given(memberRepository.findAllByGameId(gameId)).willReturn(members);
//    given(postService.createMemberDtos(members)).willReturn(createdMemberDtos);
//
//    Integer targetParticipantsCount = 3;
//    List<Role> roles = List.of(
//        new Role(1L, gameId, team1Id, "팀 1의 포지션1", targetParticipantsCount),
//        new Role(2L, gameId, team1Id, "팀 1의 포지션2", targetParticipantsCount),
//        new Role(3L, gameId, team2Id, "팀 2의 포지션1", targetParticipantsCount)
//    );
//    List<RoleDto> createdRoleDtos = List.of(
//        new RoleDto(
//            1L,
//            team1Id,
//            "팀 1의 포지션1",
//            countMembers(createdMemberDtos, team1Id),
//            targetParticipantsCount),
//        new RoleDto(
//            2L,
//            team1Id,
//            "팀 1의 포지션2",
//            countMembers(createdMemberDtos, team1Id),
//            targetParticipantsCount),
//        new RoleDto(
//            3L,
//            team2Id,
//            "팀 2의 포지션1",
//            countMembers(createdMemberDtos, team1Id),
//            targetParticipantsCount)
//    );
//    given(roleRepository.findAllByGameId(gameId)).willReturn(roles);
////    given(postService.createRoleDto()).willReturn();
//
//    Integer targetMembersCountOfTeam1 = reduceTargetMembersCount(roles, team1Id);
//    Integer targetMembersCountOfTeam2 = reduceTargetMembersCount(roles, team2Id);
//    List<Team> teams = List.of(
//        new Team(1L, gameId, "팀 1", targetMembersCountOfTeam1),
//        new Team(2L, gameId, "팀 2", targetMembersCountOfTeam2)
//    );
//    given(teamRepository.findAllByGameId(gameId)).willReturn(teams);
////    given(postService.createTeamDto()).willReturn();
//
//
//    Place place = new Place(1L, gameId, "운동 진행 장소");
//    given(placeRepository.findByGameId(gameId)).willReturn(place);
//
//    Long postId = 1L;
//    Integer cost = 10000;
//    Game game = new Game(
//        1L,
//        postId,
//        "운동 이름",
//        "운동 날짜",
//        "운동 종류(연습/경기 등)",
//        "운동 수준(입문/아마추어/중상급 등)",
//        "운동 참가자 요구성별",
//        cost
//    );
//    given(gameRepository.findByPostId(postId)).willReturn(Optional.of(game));
//
//    User author = new User(authorId, "황인우", 10.0);
//    given(userRepository.findById(authorId)).willReturn(Optional.of(author));
//
//    Boolean isThumbnailImage = true;
//    List<Image> images = List.of(
//        new Image(1L, postId, "Image Url 1", isThumbnailImage),
//        new Image(2L, postId, "Image Url 2", !isThumbnailImage)
//    );
//    given(imageRepository.findAllByPostId(postId)).willReturn(images);
//
//    Integer hits = 255;
//    String detail = "운동 모집 게시글에 들어갈 상세 내용입니다.";
//    Post post = new Post(1L, authorId, "운동 참가자 모집", hits, detail);
//
//    PostDto postDto = postService.createPostDto(post);
//
//    assertThat(postDto).isNotNull();
//    assertThat(postDto.getAuthor()).isEqualTo("황인우");
//    assertThat(postDto.getImages().get(0).getIsThumbnailImage()).isTrue();
//
//    GameDto gameDto = postDto.getGame();
//    assertThat(gameDto).isNotNull();
//    assertThat(gameDto.getExercise()).isEqualTo("운동 이름");
//    assertThat(gameDto.getPlace()).isEqualTo("운동 진행 장소");
//
//    List<TeamDto> teamDtos = gameDto.getTeams();
//    assertThat(teamDtos).hasSize(2);
//    Integer targetMembersCountSum = teamDtos.stream()
//        .map(TeamDto::getTargetMembersCount)
//        .reduce(0, Integer::sum);
//    assertThat(targetMembersCountSum).isEqualTo(9);
//
//    teamDtos.forEach(
//        teamDto -> {
//          List<RoleDto> roleDtos = teamDto.getRoles();
//          assertThat(roleDtos).isNotEmpty();
//
//          roleDtos.forEach(
//              roleDto -> {
//                List<MemberDto> memberDtos = roleDto.getMembers();
//                assertThat(memberDtos).isNotNull();
//              }
//          );
//        }
//    );
//  }

  public Integer reduceTargetMembersCount(List<Role> roles, Long teamId) {
    return roles.stream()
        .filter(role -> role.teamId().equals(teamId))
        .map(Role::targetParticipantsCount)
        .reduce(0, Integer::sum);
  }

  @Test
  void createMemberDto() {
    Long userId = 1L;
    User user = new User(userId, "김용기", 10.0);
    given(userRepository.findById(1L)).willReturn(Optional.of(user));
    Long memberId = 1L;
    Long gameId = 1L;
    Long teamId = 1L;
    Long roleId = 1L;
    Member member = new Member(memberId, gameId, teamId, roleId, userId);

    MemberDto memberDto = postService.createMemberDto(member);
    assertThat(memberDto.getName()).isEqualTo("김용기");
  }

  @Test
  void createRoleDto() {
    Long gameId = 1L;
    Long roleId = 1L;
    List<MemberDto> memberDtos = List.of(
        new MemberDto(1L, gameId, roleId, "김용기", 10.0),
        new MemberDto(2L, gameId, roleId, "길민종", 9.0),
        new MemberDto(3L, gameId, 2L, "김동우", 8.0)
    );
    Long teamId = 1L;
    Integer targetParticipantsCount = 3;
    Role role = new Role(1L, gameId, teamId, "리시버", targetParticipantsCount);

    RoleDto roleDto = postService.createRoleDto(memberDtos, role);
    assertThat(roleDto.getMembers().size()).isEqualTo(2);
  }

  @Test
  void createTeamDto() {
    Long teamId = 1L;
    Integer currentParticipants = 1;
    Integer targetParticipantsCount = 5;
    List<RoleDto> roleDtos = List.of(
        new RoleDto(
            1L,
            teamId,
            "내야수",
            currentParticipants,
            targetParticipantsCount,
            new ArrayList<>()
        ),
        new RoleDto(
            2L,
            teamId,
            "외야수",
            currentParticipants,
            targetParticipantsCount,
            new ArrayList<>()
        )
    );
    Long gameId = 1L;
    Team team = new Team(teamId, gameId, "A팀", 10);

    TeamDto teamDto = postService.createTeamDto(roleDtos, team);
    assertThat(teamDto.getMembersCount()).isEqualTo(2);
  }

  @Test
  void posts() {

  }

  @Test
  void post() {

  }
}
