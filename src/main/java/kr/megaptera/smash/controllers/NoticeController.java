package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.services.GetNoticesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notices")
public class NoticeController {
    private final GetNoticesService getNoticesService;

    public NoticeController(GetNoticesService getNoticesService) {
        this.getNoticesService = getNoticesService;
    }

    @GetMapping
    public NoticesDto notices(
        @RequestAttribute("userId") Long currentUserId
    ) {
        return getNoticesService.findAllNoticesOfUser(currentUserId);
    }
}
