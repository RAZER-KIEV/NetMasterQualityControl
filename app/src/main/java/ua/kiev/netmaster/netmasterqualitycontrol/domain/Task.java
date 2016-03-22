package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Arrays;
import java.util.Date;

import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskPriority;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskStatus;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskType;

/**
 * Created by ПК on 08.12.2015.
 */

public class Task {

    private Long taskId;
    private Long networkId;
    private Long[] executerIds;
    private TaskType taskType;
    //private String title;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private Date published;
    private Date accepted;
    private Date done;
    private TaskStatus status;              // "new", "inProcess", "done"; todo: go to enums;
    private TaskPriority priority;            // "norm", "low", "high";  todo: go to enums;

    public Task() {
    }

    public Task(Long taskId, Long networkId, Long[] executerIds, TaskType taskType, String description, String address, Double latitude, Double longitude, Date published, Date accepted, Date done, TaskStatus status, TaskPriority priority) {
        this.taskId = taskId;
        this.networkId = networkId;
        this.executerIds = executerIds;
        this.taskType = taskType;
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
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
                ", taskType=" + taskType +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", published=" + published +
                ", accepted=" + accepted +
                ", done=" + done +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
