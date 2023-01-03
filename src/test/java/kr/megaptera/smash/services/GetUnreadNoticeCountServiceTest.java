package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.UnreadNoticeCountDto;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.models.NoticeStatus;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetUnreadNoticeCountServiceTest {
    private GetUnreadNoticeCountService getUnreadNoticeCountService;

    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        noticeRepository = mock(NoticeRepository.class);

        getUnreadNoticeCountService
            = new GetUnreadNoticeCountService(noticeRepository);
    }

    @Test
    void countUnreadNotices() {
        Long currentUserId = 1L;

        List<Notice> notices = List.of(
            Notice.fake(NoticeStatus.unread()),
            Notice.fake(NoticeStatus.unread()),
            Notice.fake(NoticeStatus.unread()),
            Notice.fake(NoticeStatus.read()),
            Notice.fake(NoticeStatus.deleted())
        );

        given(noticeRepository.findAllByUserId(currentUserId))
            .willReturn(notices);

        UnreadNoticeCountDto unreadNoticeCountDto
            = getUnreadNoticeCountService.countUnreadNotices(currentUserId);

        assertThat(unreadNoticeCountDto).isNotNull();
        Long count = unreadNoticeCountDto.getCount();
        assertThat(count).isEqualTo(3);
    }
}
