package kr.megaptera.smash.dtos;

public class PostNotFoundErrorDto {
  private final String errorMessage;

  public PostNotFoundErrorDto(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
