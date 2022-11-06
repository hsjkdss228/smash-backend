package kr.megaptera.smash.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {
  private final Algorithm algorithm;

  public JwtUtil(String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String encode(Long userId) {
    return JWT.create()
        .withClaim("userId", userId)
        .sign(algorithm);
  }

  public Long decode(String token) {
    JWTVerifier verifier = JWT.require(algorithm)
        .build();
    DecodedJWT verified = verifier.verify(token);
    return verified.getClaim("userId").asLong();
  }
}
