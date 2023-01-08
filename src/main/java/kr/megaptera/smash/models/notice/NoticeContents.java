package kr.megaptera.smash.models.notice;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NoticeContents {
    @Column(name = "title")
    private String title;

    @Column(name = "detail")
    private String detail;

    private NoticeContents() {

    }

    public NoticeContents(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public String title() {
        return title;
    }

    public String detail() {
        return detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        };
        if (o == null || getClass() != o.getClass()) {
            return false;
        };
        NoticeContents that = (NoticeContents) o;
        return Objects.equals(title, that.title) && Objects.equals(detail, that.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, detail);
    }

    @Override
    public String toString() {
        return "NoticeContents{" +
            "title='" + title + '\'' +
            ", detail='" + detail + '\'' +
            '}';
    }
}
