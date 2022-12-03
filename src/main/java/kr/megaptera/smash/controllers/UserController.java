package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.UserNameDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetUserNameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    private final GetUserNameService getUserNameService;

    public UserController(GetUserNameService getUserNameService) {
        this.getUserNameService = getUserNameService;
    }

    @GetMapping("me")
    public UserNameDto userName(
        @RequestAttribute("userId") Long currentUserId
    ) {
        return getUserNameService.getUserName(currentUserId);
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFound() {
        return "User Not Found";
    }
}
