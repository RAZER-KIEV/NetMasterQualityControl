package ua.kiev.netmaster.netmasterqualitycontrol.domain;

/**
 * Created by RAZER on 11-Mar-16.
 */
public class Office {
    private Long officeId;
    private Long networkId;
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;

    public Office() {
    }

    public Office(Long officeId, Long networkId, String name, String description, String address, Double latitude, Double longitude) {
        this.officeId = officeId;
        this.networkId = networkId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "Office{" +
                "officeId=" + officeId +
                ", networkId=" + networkId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
