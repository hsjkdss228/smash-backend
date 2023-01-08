package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.NoticeNotFound;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.models.notice.NoticeStatus;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ReadNoticeServiceTest {
    private ReadNoticeService readNoticeService;

    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        noticeRepository = mock(NoticeRepository.class);
        readNoticeService = new ReadNoticeService(noticeRepository);
    }

    @Test
    void readTargetNotice() {
        Notice notice = spy(Notice.fake(NoticeStatus.unread()));
        Long targetNoticeId = notice.id();
        given(noticeRepository.findById(targetNoticeId))
            .willReturn(Optional.of(notice));

        readNoticeService.readTargetNotice(targetNoticeId);

        verify(notice).read();
    }

    @Test
    void noticeNotFound() {
        Long wrongNoticeId = 99L;
        given(noticeRepository.findById(wrongNoticeId))
            .willThrow(NoticeNotFound.class);

        assertThrows(NoticeNotFound.class, () -> {
            readNoticeService.readTargetNotice(wrongNoticeId);
        });
    }
}
