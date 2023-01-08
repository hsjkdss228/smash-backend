package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.NoticeNotFound;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReadNoticeService {
    private final NoticeRepository noticeRepository;

    public ReadNoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public void readTargetNotice(Long targetNoticeId) {
        Notice notice = noticeRepository.findById(targetNoticeId)
            .orElseThrow(() -> new NoticeNotFound(targetNoticeId));

        notice.read();
    }
}
