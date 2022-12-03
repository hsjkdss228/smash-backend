package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.NoticeListDto;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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

    public NoticeListDto toListDto() {
        return new NoticeListDto(
            id,
            createdAt.toString(),
            contents.title()
        );
    }
}
