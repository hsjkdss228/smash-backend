package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.UnreadNoticeCountDto;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetUnreadNoticeCountService {
    private final NoticeRepository noticeRepository;

    public GetUnreadNoticeCountService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public UnreadNoticeCountDto countUnreadNotices(Long currentUserId) {
        List<Notice> notices = noticeRepository.findAllByUserId(currentUserId);

        Long unreadNoticeCount = notices.stream()
            .filter(Notice::unchecked)
            .count();

        return new UnreadNoticeCountDto(unreadNoticeCount);
    }
}
