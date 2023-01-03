package kr.megaptera.smash.interceptors;

import com.auth0.jwt.exceptions.JWTDecodeException;
import kr.megaptera.smash.exceptions.AuthenticationError;
import kr.megaptera.smash.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return true;
        }

        String accessToken = authorization.substring("Bearer ".length());

        if (accessToken.equals("null")) {
            return true;
        }

        try {
            Long userId = jwtUtil.decode(accessToken);

            request.setAttribute("userId", userId);
            return true;
        } catch (JWTDecodeException exception) {
            throw new AuthenticationError();
        }
    }
}
