package kr.megaptera.smash.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Member {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "TEAM_ID")
  private Team teamIn;

  @OneToOne
  @JoinColumn(name = "PERSON_ID")
  private User person;

  @ManyToOne
  @JoinColumn(name = "POSITION_ID")
  private Role roleIn;

  public Member() {

  }

  public Member(Long id, Team teamIn, User person, Role roleIn) {
    this.id = id;
    this.teamIn = teamIn;
    this.person = person;
    this.roleIn = roleIn;
  }
}
