package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NoticeStatus {
    private static final NoticeStatus UNREAD
         = new NoticeStatus("unread");
    private static final NoticeStatus READ
        = new NoticeStatus("read");
    private static final NoticeStatus DELETED
        = new NoticeStatus("deleted");

    @Column(name = "status")
    private String value;

    private NoticeStatus() {

    }

    public NoticeStatus(String value) {
        this.value = value;
    }

    public static NoticeStatus unread() {
        return UNREAD;
    }

    public static NoticeStatus read() {
        return READ;
    }

    public static NoticeStatus deleted() {
        return DELETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoticeStatus that = (NoticeStatus) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
