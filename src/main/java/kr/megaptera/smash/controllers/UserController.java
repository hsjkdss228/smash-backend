package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.SignUpRequestDto;
import kr.megaptera.smash.dtos.SignUpResultDto;
import kr.megaptera.smash.dtos.UserNameDto;
import kr.megaptera.smash.exceptions.AlreadyRegisteredUsername;
import kr.megaptera.smash.exceptions.SignUpFailed;
import kr.megaptera.smash.exceptions.UnmatchedPasswordAndAConfirmPassword;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetUserNameService;
import kr.megaptera.smash.services.SignUpService;
import kr.megaptera.smash.validations.ValidationSequence;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private final GetUserNameService getUserNameService;
    private final SignUpService signUpService;

    public UserController(GetUserNameService getUserNameService,
                          SignUpService signUpService) {
        this.getUserNameService = getUserNameService;
        this.signUpService = signUpService;
    }

    @GetMapping("me")
    public UserNameDto userName(
        @RequestAttribute("userId") Long currentUserId
    ) {
        return getUserNameService.getUserName(currentUserId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResultDto signUp(
        @Validated(value = {ValidationSequence.class})
        @RequestBody SignUpRequestDto signUpRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

            throw new SignUpFailed(errorMessages);
        }

        return signUpService.createUser(
            signUpRequestDto.getName(),
            signUpRequestDto.getUsername(),
            signUpRequestDto.getPassword(),
            signUpRequestDto.getConfirmPassword(),
            signUpRequestDto.getGender(),
            signUpRequestDto.getPhoneNumber()
        );
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFound() {
        return "User Not Found";
    }

    @ExceptionHandler(SignUpFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String signUpFailed(SignUpFailed exception) {
        return exception.errorMessages().get(0);
    }

    @ExceptionHandler(AlreadyRegisteredUsername.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String alreadyRegisteredUsername(
        AlreadyRegisteredUsername exception
    ) {
        return exception.getMessage();
    }

    @ExceptionHandler(UnmatchedPasswordAndAConfirmPassword.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String unmatchedPasswordAndConfirmPassword(
        UnmatchedPasswordAndAConfirmPassword exception
    ) {
        return exception.getMessage();
    }
}
