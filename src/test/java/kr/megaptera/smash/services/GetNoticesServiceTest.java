package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.NoticeDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.models.NoticeContents;
import kr.megaptera.smash.models.NoticeStatus;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetNoticesServiceTest {
    private GetNoticesService getNoticesService;

    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        noticeRepository = mock(NoticeRepository.class);
        getNoticesService = new GetNoticesService(noticeRepository);
    }

    @Test
    void findAllNoticesOfUser() {
        // TODO: 필요한 인터페이스를 고려해 fake() 형태로 대체하기

        Long userId = 1L;

        List<Notice> notices = List.of(
            new Notice(
                1L,
                userId,
                new NoticeContents(
                    "알림 제목",
                    "알림 상세 내용"
                ),
                NoticeStatus.unread(),
                LocalDateTime.now()
            ),
            new Notice(
                2L,
                userId,
                new NoticeContents(
                    "알림 제목",
                    "알림 상세 내용"
                ),
                NoticeStatus.read(),
                LocalDateTime.now()
            ),
            new Notice(
                3L,
                userId,
                new NoticeContents(
                    "알림 제목",
                    "알림 상세 내용"
                ),
                NoticeStatus.deleted(),
                LocalDateTime.now()
            )
        );

        given(noticeRepository.findAllByUserId(userId))
            .willReturn(notices);

        NoticesDto noticesDto
            = getNoticesService.findAllNoticesOfUser(userId);

        assertThat(noticesDto).isNotNull();
        List<NoticeDto> noticeDtos = noticesDto.getNotices();
        assertThat(noticeDtos).hasSize(2);
        // TODO: 시간 변환 로직을 구현할 경우 시간 변환이 정상적으로 되었는지 테스트
        assertThat(noticeDtos.get(1).getTitle()).isEqualTo("알림 제목");
    }
}
