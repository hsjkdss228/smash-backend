package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.TeamDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Team {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "POST_ID")
  private Post postIn;

  private String name;

  private Integer targetMembersCount;

  @OneToMany(mappedBy = "teamIn")
  private List<Member> membersUnder;

  @OneToMany(mappedBy = "teamIn")
  private List<Role> rolesUnder;

  public Team() {

  }

  public Team(Long id, Post postIn, List<Member> membersUnder, Integer targetMembersCount) {
    this.id = id;
    this.postIn = postIn;
    this.membersUnder = membersUnder;
    this.targetMembersCount = targetMembersCount;
  }

  public Team(Long id) {
    this.id = id;
  }

  public TeamDto toTeamDto() {
    return new TeamDto(
        id,
        postIn.id(),
        name,
        membersUnder.size(),
        targetMembersCount
    );
  }

  public Long id() {
    return id;
  }
}
