package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.NoticeDto;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notices")
public class Notice {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Embedded
    private NoticeContents contents;

    @Embedded
    private NoticeStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Notice() {

    }

    public Notice(Long userId,
                  NoticeContents contents,
                  NoticeStatus status
    ) {
        this.userId = userId;
        this.contents = contents;
        this.status = status;
    }

    public Notice(Long id,
                  Long userId,
                  NoticeContents contents,
                  NoticeStatus status,
                  LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.contents = contents;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public NoticeContents contents() {
        return contents;
    }

    public NoticeStatus status() {
        return status;
    }

    public boolean active() {
        return status.equals(NoticeStatus.unread())
            || status.equals(NoticeStatus.read());
    }

    public boolean unchecked() {
        return status.equals(NoticeStatus.unread());
    }

    public void read() {
        status = NoticeStatus.read();
    }

    public void deleted() {
        status = NoticeStatus.deleted();
    }

    public static Notice fake(NoticeStatus status) {
        Long userId = 1L;
        return new Notice(
            1L,
            userId,
            new NoticeContents(
                "알림 제목",
                "알림 상세 내용"
            ),
            status,
            LocalDateTime.now()
        );
    }

    public static Notice fake(Long noticeId, NoticeStatus status) {
        Long userId = 1L;
        return new Notice(
            noticeId,
            userId,
            new NoticeContents(
                "알림 제목",
                "알림 상세 내용"
            ),
            status,
            LocalDateTime.now()
        );
    }

    public static List<Notice> fakesUnread(long generationCount) {
        Long userId = 1L;
        List<Notice> notices = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Notice notice = new Notice(
                id,
                userId,
                new NoticeContents(
                    "알림 제목",
                    "알림 상세 내용"
                ),
                NoticeStatus.unread(),
                LocalDateTime.now()
            );
            notices.add(notice);
        }
        return notices;
    }

    public NoticeDto toDto() {
        return new NoticeDto(
            id,
            status.toString(),
            createdAt.toString(),
            contents.title(),
            contents.detail()
        );
    }
}
