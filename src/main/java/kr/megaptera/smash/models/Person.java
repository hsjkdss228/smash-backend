package kr.megaptera.smash.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERSON")
public class Person {
  @Id
  @GeneratedValue
  private Long id;

  // TODO: 성별이 추가되어야 함

  private Person() {

  }
}
