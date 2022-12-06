package kr.megaptera.smash.exceptions;

public class NoticeNotFound extends RuntimeException {
    public NoticeNotFound(Long noticeId) {
        super("주어진 식별자에 해당하는 알림을 찾을 수 없습니다: " + noticeId);
    }
}
