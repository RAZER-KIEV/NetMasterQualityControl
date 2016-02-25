package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Arrays;
import java.util.Date;

import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskPriority;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskStatus;

/**
 * Created by ПК on 08.12.2015.
 */

public class Task {

    private Long taskId;
    private Long networkId;
    private Long[] executerIds;
    private String title;
    private String description;
    private String address;
    private String latitude;
    private String longitude;
    private Date published;
    private Date accepted;
    private Date done;
    private TaskStatus status;              // "new", "inProcess", "done"; todo: go to enums;
    private TaskPriority priority;            // "norm", "low", "high";  todo: go to enums;

    public Task() {
    }

    public Task(Long taskId, Long networkId, Long[] executerIds, String title, String description, String address, String latitude, String longitude, Date published, Date accepted, Date done, TaskStatus status, TaskPriority priority) {
        this.taskId = taskId;
        this.networkId = networkId;
        this.executerIds = executerIds;
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


    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", networkId=" + networkId +
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
