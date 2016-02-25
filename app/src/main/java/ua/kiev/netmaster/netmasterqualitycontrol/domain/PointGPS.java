package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Date;

/**
 * Created by ПК on 03.12.2015.
 */

public class PointGPS {
    private Long id;
    private Long emplId;
    private String title;
    private Double latitude;
    private Double longitude;
    private Date localDateTime;

    public PointGPS() {
    }

    public PointGPS(Long emplId, String title, Double latitude, Double longitude) {
        this.emplId = emplId;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localDateTime = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmplId() {
        return emplId;
    }

    public void setEmplId(Long emplId) {
        this.emplId = emplId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    @Override
    public String toString() {
        return "PointGPS{" +
                "id=" + id +
                ", emplId=" + emplId +
                ", title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", publishDate=" + localDateTime +
                '}';
    }
}
