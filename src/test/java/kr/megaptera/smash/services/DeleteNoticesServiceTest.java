package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.NoticeNotFound;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.models.notice.NoticeStatus;
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

class DeleteNoticesServiceTest {
    private DeleteNoticesService deleteNoticesService;

    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        noticeRepository = mock(NoticeRepository.class);

        deleteNoticesService = new DeleteNoticesService(noticeRepository);
    }

    @Test
    void deleteTargetNotices() {
        Long noticeId1 = 3L;
        Long noticeId2 = 4L;
        Notice notice1 = spy(Notice.fake(noticeId1, NoticeStatus.unread()));
        Notice notice2 = spy(Notice.fake(noticeId2, NoticeStatus.read()));

        given(noticeRepository.findById(noticeId1))
            .willReturn(Optional.of(notice1));
        given(noticeRepository.findById(noticeId2))
            .willReturn(Optional.of(notice2));

        List<Long> noticeIds = List.of(
            noticeId1,
            noticeId2
        );

        deleteNoticesService.deleteTargetNotices(noticeIds);

        verify(noticeRepository).findById(noticeId1);
        verify(noticeRepository).findById(noticeId2);
        verify(notice1).deleted();
        verify(notice2).deleted();
    }

    @Test
    void noticeNotFound() {
        Long noticeId1 = 234L;
        Long noticeId2 = 567L;

        given(noticeRepository.findById(noticeId1))
            .willThrow(NoticeNotFound.class);

        List<Long> noticeIds = List.of(
            noticeId1,
            noticeId2
        );

        assertThrows(NoticeNotFound.class, () -> {
            deleteNoticesService.deleteTargetNotices(noticeIds);
        });
    }
}
