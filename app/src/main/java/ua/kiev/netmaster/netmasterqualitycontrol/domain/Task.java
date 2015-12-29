package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by ПК on 08.12.2015.
 */

public class Task {

    private Long taskId;
    private Long[] executerIds;
    private String title;
    private String description;
    private String address;
    private String latitude;
    private String longitude;
    private Date published;
    private Date accepted;
    private Date done;
    private String status;              // "new", "inProcess", "done"; todo: go to enums;
    private String priority;            // "norm", "low", "high";  todo: go to enums;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        status = "new";
        priority = "norm";
        published = new Date();
    }

    public Task(Long taskId, Long[] executersId, String title, String description, String address, String latitude, String longitude, Date published, Date accepted, Date done, String status, String priority) {
        this.taskId =taskId;
        this.executerIds = executersId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.published = published;
        this.accepted = accepted;
        this.done = done;
        this.status = status;
        this.priority = priority;
    }

    public Task(Long[] executersId, String title, String description, String address, String latitude, String longitude, Date published, Date accepted, Date done, String status, String priority) {
        this.executerIds = executersId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.published = published;
        this.accepted = accepted;
        this.done = done;
        this.status = status;
        this.priority = priority;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long[] getExecuterIds() {
        return executerIds;
    }

    public void setExecuterIds(Long[] executerIds) {
        this.executerIds = executerIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getAccepted() {
        return accepted;
    }

    public void setAccepted(Date accepted) {
        this.accepted = accepted;
    }

    public Date getDone() {
        return done;
    }

    public void setDone(Date done) {
        this.done = done;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", executerIds=" + Arrays.toString(executerIds) +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", published=" + published +
                ", accepted=" + accepted +
                ", done=" + done +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
