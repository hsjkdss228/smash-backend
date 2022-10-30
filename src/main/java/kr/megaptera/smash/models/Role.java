package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.RoleDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Role {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
  private Team teamIn;

  private String name;

  private Integer targetParticipantsCount;

  @OneToMany(mappedBy = "roleIn")
  private List<Member> participantsUnder;

  public Role() {

  }

  public Role(Long id, Team teamIn, String name, Integer targetParticipantsCount, List<Member> participantsUnder) {
    this.id = id;
    this.teamIn = teamIn;
    this.name = name;
    this.targetParticipantsCount = targetParticipantsCount;
    this.participantsUnder = participantsUnder;
  }

  public RoleDto toRoleDto() {
    return new RoleDto(
        id,
        teamIn.id(),
        name,
        participantsUnder.size(),
        targetParticipantsCount
    );
  }
}
