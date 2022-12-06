package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeContentsTest {
    @Test
    void equals() {
        assertThat(new NoticeContents("알림 제목", "알림 내용"))
            .isEqualTo(new NoticeContents("알림 제목", "알림 내용"));
        assertThat(new NoticeContents("알림 제목 1", "알림 내용 1"))
            .isNotEqualTo(new NoticeContents("알림 제목 2", "알림 내용 2"));
        assertThat(new NoticeContents("알림 제목", "알림 내용"))
            .isNotEqualTo(null);
    }
}
