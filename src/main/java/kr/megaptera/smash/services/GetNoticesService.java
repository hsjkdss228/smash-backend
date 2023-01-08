package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.NoticeDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.models.notice.Notice;
import kr.megaptera.smash.repositories.NoticeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetNoticesService {
    private final NoticeRepository noticeRepository;

    public GetNoticesService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public NoticesDto findAllNoticesOfUser(Long currentUserId) {
        List<Notice> notices
            = noticeRepository.findAllByUserId(
                currentUserId, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<NoticeDto> noticeDtos = notices.stream()
            .filter(Notice::active)
            .map(Notice::toDto)
            .toList();

        return new NoticesDto(noticeDtos);
    }
}
