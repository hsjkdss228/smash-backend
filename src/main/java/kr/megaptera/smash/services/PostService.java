package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostThumbnailDto;
import kr.megaptera.smash.dtos.PostThumbnailsDto;
import kr.megaptera.smash.dtos.RoleDto;
import kr.megaptera.smash.dtos.TeamDto;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.Role;
import kr.megaptera.smash.models.Team;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.RoleRepository;
import kr.megaptera.smash.repositories.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
  private final PostRepository postRepository;
  private final TeamRepository teamRepository;
  private final RoleRepository roleRepository;

  public PostService(PostRepository postRepository,
                     TeamRepository teamRepository,
                     RoleRepository roleRepository) {
    this.postRepository = postRepository;
    this.teamRepository = teamRepository;
    this.roleRepository = roleRepository;
  }

  public PostThumbnailsDto posts() {
    List<Post> posts = postRepository.findAll();
    List<Team> teams = teamRepository.findAll();
    List<Role> roles = roleRepository.findAll();

    List<PostThumbnailDto> postThumbnailDto = posts.stream()
        .map(Post::toThumbnailDto)
        .toList();
    List<TeamDto> teamDto = teams.stream()
        .map(Team::toTeamDto)
        .toList();
    List<RoleDto> roleDto = roles.stream()
        .map(Role::toRoleDto)
        .toList();

    return new PostThumbnailsDto(postThumbnailDto, teamDto, roleDto);
  }
}
