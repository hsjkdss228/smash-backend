package kr.megaptera.smash.controllers;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@WebMvcTest(PostController.class)
class PostControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @MockBean
  private TeamService teamService;

  @MockBean
  private RoleService roleService;

  @MockBean
  private MemberService memberService;

  @MockBean
  private PlaceService placeService;

  @MockBean
  private ImageService imageService;

  @MockBean
  private UserService userService;

  @Test
  void posts() throws Exception {
    List<User> users = List.of(
        new User(1L, "User 1", 7.5),
        new User(2L, "User 2", 5.0),
        new User(3L, "User 3", 8.0),
        new User(4L, "User 4", 9.0),
        new User(5L, "User 5", 5.0),
        new User(6L, "Author 1", 10.0),
        new User(7L, "Author 2", 7.7)
    );

    List<Post> posts = List.of(
        // id, userId
        new Post(1L, 1L, "Gathering", 15, "Play baseball with me"),
        new Post(2L, 2L, "Gathering", 30, "Play football with me")
    );
    given(postService.posts()).willReturn(posts);
    given(userService.user(1L)).willReturn(users.get(5));
    given(imageService.imagesByPost(1L)).willReturn((
        List.of(
            new Image(1L, 1L, "Main Baseball Image Url", true),
            new Image(2L, 1L, "Sub Baseball Image Url", false)
        )));
    given(userService.user(2L)).willReturn(users.get(6));
    given(imageService.imagesByPost(2L)).willReturn((
        List.of(
            new Image(3L, 2L, "Main Soccer Image Url", true)
        )));

    List<Team> teams = List.of(
        // id, postId
        new Team(1L, 1L, "Team 1",
            "Baseball", "11월 1일 18:00 ~ 21:00",
            "Practice", "Amateur", "Male",
            12, 10000),
        new Team(2L, 2L, "Team 1",
            "Soccer", "11월 2일 15:00 ~ 17:00",
            "Actual Game", "Professional", "mixed",
            6, 5000)
    );
    given(teamService.teams()).willReturn(teams);
    given(memberService.membersCountByTeam(1L)).willReturn(4);
    given(memberService.membersCountByTeam(2L)).willReturn(1);

    List<Role> roles = List.of(
        // id, teamId
        new Role(1L, 1L, "Pitcher", 3),
        new Role(2L, 1L, "Infielder", 5),
        new Role(3L, 1L, "Outfielder", 4),
        new Role(4L, 2L, "Free", 6));
    given(roleService.roles()).willReturn(roles);
    given(memberService.membersCountByRole(1L)).willReturn(0);
    given(memberService.membersCountByRole(2L)).willReturn(2);
    given(memberService.membersCountByRole(3L)).willReturn(2);
    given(memberService.membersCountByRole(4L)).willReturn(1);

    List<Member> members = List.of(
        // id, teamId, roleId, userId
        new Member(1L, 1L, 2L, 1L),
        new Member(2L, 1L, 2L, 2L),
        new Member(3L, 1L, 3L, 3L),
        new Member(4L, 1L, 3L, 4L),
        new Member(5L, 2L, 4L, 5L)
    );
    given(memberService.members()).willReturn(members);
    given(userService.user(1L)).willReturn(users.get(0));
    given(userService.user(2L)).willReturn(users.get(1));
    given(userService.user(3L)).willReturn(users.get(2));
    given(userService.user(4L)).willReturn(users.get(3));
    given(userService.user(5L)).willReturn(users.get(4));

    List<Place> places = List.of(
        // id, teamId
        new Place(1L, 1L, "Guui Baseball Park"),
        new Place(2L, 2L, "Jayang Middle School"));
    given(placeService.placesInPosts(posts)).willReturn(places);

    mockMvc.perform(MockMvcRequestBuilders.get("/posts/list"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
