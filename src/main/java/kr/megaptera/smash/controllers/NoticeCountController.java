package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.UnreadNoticeCountDto;
import kr.megaptera.smash.services.GetUnreadNoticeCountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notice-count")
public class NoticeCountController {
    private final GetUnreadNoticeCountService getUnreadNoticeCountService;

    public NoticeCountController(GetUnreadNoticeCountService getUnreadNoticeCountService) {
        this.getUnreadNoticeCountService = getUnreadNoticeCountService;
    }

    @GetMapping
    public UnreadNoticeCountDto unreadNoticeCount(
        @RequestAttribute("userId") Long currentUserId,
        @RequestParam(value = "status") String status
    ) {
        return getUnreadNoticeCountService.countUnreadNotices(currentUserId);
    }
}
