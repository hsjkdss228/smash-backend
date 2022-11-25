package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class GameDateTime {
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private GameDateTime() {

    }

    public GameDateTime(LocalDate date,
                        LocalTime startTime,
                        LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate date() {
        return date;
    }

    public LocalTime startTime() {
        return startTime;
    }

    public LocalTime endTime() {
        return endTime;
    }

    public String joinDateAndTime() {
        String startHour = formatTime(startTime.getHour());
        String startMinute = formatTime(startTime.getMinute());
        String endHour = formatTime(endTime.getHour());
        String endMinute = formatTime(endTime.getMinute());

        return date.getYear() + "년 "
            + date.getMonthValue() + "월 "
            + date.getDayOfMonth() + "일 "
            + startHour + ":"
            + startMinute + "~"
            + endHour + ":"
            + endMinute;
    }

    private String formatTime(int time) {
        return time < 10
            ? "0" + time
            : Integer.toString(time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDateTime that = (GameDateTime) o;
        return Objects.equals(date, that.date)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime);
    }

    @Override
    public String toString() {
        return "GameDateTime{" +
            "date=" + date +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }
}
