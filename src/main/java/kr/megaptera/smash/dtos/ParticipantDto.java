package kr.megaptera.smash.dtos;

public class ParticipantDto {
  private final Long participantId;

  private final String name;

  public ParticipantDto(Long participantId, String name) {
    this.participantId = participantId;
    this.name = name;
  }

  public Long getParticipantId() {
    return participantId;
  }

  public String getName() {
    return name;
  }
}
