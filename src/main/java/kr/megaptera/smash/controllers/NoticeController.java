package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.services.GetNoticesService;
import kr.megaptera.smash.services.ReadNoticeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notices")
public class NoticeController {
    private final GetNoticesService getNoticesService;
    private final ReadNoticeService readNoticeService;

    public NoticeController(GetNoticesService getNoticesService,
                            ReadNoticeService readNoticeService) {
        this.getNoticesService = getNoticesService;
        this.readNoticeService = readNoticeService;
    }

    @GetMapping
    public NoticesDto notices(
        @RequestAttribute("userId") Long currentUserId
    ) {
        return getNoticesService.findAllNoticesOfUser(currentUserId);
    }

    @PatchMapping("{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readNotice(
        @PathVariable("noticeId") Long targetNoticeId
    ) {
        readNoticeService.readTargetNotice(targetNoticeId);
    }
}
