package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.NoticeNotFound;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.models.NoticeStatus;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ReadNoticesServiceTest {
    private ReadNoticesService readNoticesService;

    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        noticeRepository = mock(NoticeRepository.class);

        readNoticesService = new ReadNoticesService(noticeRepository);
    }

    @Test
    void readTargetNotices() {
        Long noticeId1 = 1L;
        Long noticeId2 = 2L;
        Notice notice1 = spy(Notice.fake(noticeId1, NoticeStatus.unread()));
        Notice notice2 = spy(Notice.fake(noticeId2, NoticeStatus.unread()));

        given(noticeRepository.findById(noticeId1))
            .willReturn(Optional.of(notice1));
        given(noticeRepository.findById(noticeId2))
            .willReturn(Optional.of(notice2));

        List<Long> noticeIds = List.of(
            noticeId1,
            noticeId2
        );

        readNoticesService.readTargetNotices(noticeIds);

        verify(noticeRepository).findById(noticeId1);
        verify(noticeRepository).findById(noticeId2);
        verify(notice1).read();
        verify(notice2).read();
    }

    @Test
    void noticeNotFound() {
        Long noticeId1 = 996L;
        Long noticeId2 = 2222L;

        given(noticeRepository.findById(noticeId1))
            .willThrow(NoticeNotFound.class);

        List<Long> noticeIds = List.of(
            noticeId1,
            noticeId2
        );

        assertThrows(NoticeNotFound.class, () -> {
            readNoticesService.readTargetNotices(noticeIds);
        });
    }
}
