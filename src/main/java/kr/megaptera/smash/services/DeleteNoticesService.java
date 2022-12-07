package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.NoticeNotFound;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeleteNoticesService {
    private final NoticeRepository noticeRepository;

    public DeleteNoticesService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public void deleteTargetNotices(List<Long> noticeIds) {
        noticeIds.forEach(noticeId -> {
            Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFound(noticeId));

            notice.deleted();
        });
    }
}
