package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.ImageDto;
import kr.megaptera.smash.dtos.MemberDto;
import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.dtos.PostThumbnailDto;
import kr.megaptera.smash.dtos.PostThumbnailsDto;
import kr.megaptera.smash.dtos.RoleDto;
import kr.megaptera.smash.dtos.TeamDto;
import kr.megaptera.smash.models.Image;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Role;
import kr.megaptera.smash.models.Team;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.ImageService;
import kr.megaptera.smash.services.MemberService;
import kr.megaptera.smash.services.PlaceService;
import kr.megaptera.smash.services.PostService;
import kr.megaptera.smash.services.RoleService;
import kr.megaptera.smash.services.TeamService;
import kr.megaptera.smash.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {
  private final PostService postService;
  private final UserService userService;
  private final ImageService imageService;
  private final TeamService teamService;
  private final RoleService roleService;
  private final MemberService memberService;
  private final PlaceService placeService;

  public PostController(PostService postService,
                        UserService userService,
                        ImageService imageService,
                        TeamService teamService,
                        RoleService roleService,
                        MemberService memberService,
                        PlaceService placeService) {
    this.postService = postService;
    this.userService = userService;
    this.imageService = imageService;
    this.teamService = teamService;
    this.roleService = roleService;
    this.memberService = memberService;
    this.placeService = placeService;
  }

  @GetMapping("/posts/list")
  public PostThumbnailsDto posts() {
    List<Post> posts = postService.posts();
    List<Team> teams = teamService.teams();
    List<Role> roles = roleService.roles();
    List<Member> members = memberService.members();
    List<Place> places = placeService.placesInPosts(posts);

    List<PostThumbnailDto> postThumbnailDtos = posts.stream()
        .map(post -> {
          User author = userService.user(post.userId());
          List<ImageDto> imageDtos
              = imageService.imagesByPost(post.id()).stream()
              .map(Image::toDto)
              .toList();
          return post.toThumbnailDto(author.name(), imageDtos);
        })
        .toList();

    List<TeamDto> teamDtos = teams.stream()
        .map(team -> {
          Integer membersCount = memberService.membersCountByTeam(team.id());
          return team.toDto(membersCount);
        })
        .toList();

    List<RoleDto> roleDtos = roles.stream()
        .map(role -> {
          Integer participantsCount = memberService.membersCountByRole(role.id());
          return role.toDto(participantsCount);
        })
        .toList();

    List<MemberDto> memberDtos = members.stream()
        .map(member -> {
          User participant = userService.user(member.userId());
          return member.toDto(participant.name(), participant.mannerScore());
        })
        .toList();

    List<PlaceDto> placeDtos = places.stream()
        .map(Place::toDto)
        .toList();

    return new PostThumbnailsDto(
        postThumbnailDtos,
        teamDtos,
        roleDtos,
        memberDtos,
        placeDtos
    );
  }
}
