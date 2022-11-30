package kr.megaptera.smash.controllers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import kr.megaptera.smash.dtos.LoginFailedErrorDto;
import kr.megaptera.smash.dtos.LoginRequestDto;
import kr.megaptera.smash.dtos.LoginResultDto;
import kr.megaptera.smash.exceptions.AuthenticationError;
import kr.megaptera.smash.exceptions.LoginFailed;
import kr.megaptera.smash.services.LoginService;
import kr.megaptera.smash.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("session")
public class SessionController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    public SessionController(LoginService loginService,
                             JwtUtil jwtUtil) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResultDto login(
        @Validated @RequestBody LoginRequestDto loginRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList().get(0);
            throw new LoginFailed(errorMessage);
        }

        Long userId = loginService.verifyUser(
            loginRequestDto.getUsername(),
            loginRequestDto.getPassword()
        );

        try {
            String accessToken = jwtUtil.encode(userId);
            return new LoginResultDto(accessToken);
        } catch (JWTDecodeException exception) {
            throw new AuthenticationError();
        }
    }

    @ExceptionHandler(LoginFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public LoginFailedErrorDto loginFailed(LoginFailed exception) {
        return new LoginFailedErrorDto(exception.getMessage());
    }
}
