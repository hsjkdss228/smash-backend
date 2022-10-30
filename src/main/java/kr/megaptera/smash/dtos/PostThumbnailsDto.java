package kr.megaptera.smash.dtos;

import kr.megaptera.smash.models.Role;

import java.util.List;

public class PostThumbnailsDto {
  private final List<PostThumbnailDto> posts;

  private final List<TeamDto> teams;

  private final List<RoleDto> positions;

  public PostThumbnailsDto(List<PostThumbnailDto> posts,
                           List<TeamDto> teams,
                           List<RoleDto> roles) {
    this.posts = posts;
    this.teams = teams;
    this.positions = roles;
  }

  public List<PostThumbnailDto> getPosts() {
    return posts;
  }

  public List<TeamDto> getTeams() {
    return teams;
  }

  public List<RoleDto> getPositions() {
    return positions;
  }
}
