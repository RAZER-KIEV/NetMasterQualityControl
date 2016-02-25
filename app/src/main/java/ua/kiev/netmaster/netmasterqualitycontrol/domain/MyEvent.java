package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by RAZER on 2/18/2016.
 */public class MyEvent {
    Long eventId;
    Long resevers[];
    String message;
    Date publishDate;

    public MyEvent(String message) {
        this.message = message;
    }

    public MyEvent() {
    }

    public MyEvent(Long[] resevers, String message, Date date) {
        this.resevers = resevers;
        this.message = message;
        this.publishDate = date;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long[] getResevers() {
        return resevers;
    }

    public void setResevers(Long[] resevers) {
        this.resevers = resevers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "eventId=" + eventId +
                ", resevers=" + Arrays.toString(resevers) +
                ", message='" + message + '\'' +
                ", publishDate=" + publishDate +
                '}';
    }
}