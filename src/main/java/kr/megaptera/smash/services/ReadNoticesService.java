package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.NoticeNotFound;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReadNoticesService {
    private final NoticeRepository noticeRepository;

    public ReadNoticesService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public void readTargetNotices(List<Long> noticeIds) {
        noticeIds.forEach(noticeId -> {
             Notice notice = noticeRepository.findById(noticeId)
                 .orElseThrow(() -> new NoticeNotFound(noticeId));

             notice.read();
        });
    }
}
