package kr.megaptera.smash.dtos;

import java.util.List;

public class PostThumbnailsDto {
  private final List<PostThumbnailDto> posts;

  private final List<TeamDto> teams;

  private final List<RoleDto> positions;

  private final List<MemberDto> members;

  private final List<PlaceDto> places;

  public PostThumbnailsDto(List<PostThumbnailDto> posts,
                           List<TeamDto> teams,
                           List<RoleDto> positions,
                           List<MemberDto> members,
                           List<PlaceDto> places) {
    this.posts = posts;
    this.teams = teams;
    this.positions = positions;
    this.members = members;
    this.places = places;
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

  public List<MemberDto> getMembers() {
    return members;
  }

  public List<PlaceDto> getPlaces() {
    return places;
  }
}
