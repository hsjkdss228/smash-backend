package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.NoticeListDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.repositories.NoticeRepository;
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
            = noticeRepository.findAllByUserId(currentUserId);

        // TODO: 삭제 상태의 알림들은 가져오지 않아야 한다.

        // TODO: 이 동작조차도 객체의 동작 안에 숨어야 하는 것 같다.
        //   꺼내온 알림들을 DTO로 만드는 건 도대체 누구의 책임인가?
        //   알림판? 그럼 게시판-게시물의 관계인가?
        //   동작을 수행할 적절한 객체를 찾아 정의한 뒤에는 그 객체의 동작으로 옮기기
        List<NoticeListDto> noticeListDtos = notices.stream()
            .filter(Notice::active)
            .map(Notice::toListDto)
            .toList();

        return new NoticesDto(noticeListDtos);
    }
}
