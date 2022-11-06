package kr.megaptera.smash.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {
  private static final String SECRET = "JWTSECRET";

  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil(SECRET);
  }

  @Test
  void encodeAndDecode() {
    Long userId = 1L;

    String accessToken = jwtUtil.encode(userId);
    assertThat(accessToken).contains(".");

    Long original = jwtUtil.decode(accessToken);
    assertThat(original).isEqualTo(userId);
  }
}
