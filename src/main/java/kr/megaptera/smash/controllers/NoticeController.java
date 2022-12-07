package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.NoticeIdsRequestDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.services.DeleteNoticesService;
import kr.megaptera.smash.services.GetNoticesService;
import kr.megaptera.smash.services.ReadNoticeService;
import kr.megaptera.smash.services.ReadNoticesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notices")
public class NoticeController {
    private final GetNoticesService getNoticesService;
    private final ReadNoticeService readNoticeService;
    private final ReadNoticesService readNoticesService;
    private final DeleteNoticesService deleteNoticesService;

    public NoticeController(GetNoticesService getNoticesService,
                            ReadNoticeService readNoticeService,
                            ReadNoticesService readNoticesService,
                            DeleteNoticesService deleteNoticesService
    ) {
        this.getNoticesService = getNoticesService;
        this.readNoticeService = readNoticeService;
        this.readNoticesService = readNoticesService;
        this.deleteNoticesService = deleteNoticesService;
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

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeNoticesStatus(
        @RequestBody NoticeIdsRequestDto noticeIdsRequestDto,
        @RequestParam(value = "status") String status
    ) {
        if (status.equals("read")) {
            readNoticesService.readTargetNotices(noticeIdsRequestDto.getIds());
            return;
        }
        deleteNoticesService.deleteTargetNotices(noticeIdsRequestDto.getIds());
    }
}
