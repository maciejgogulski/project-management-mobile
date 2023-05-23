package pl.kalisz.ak.pup.todolist_mobile.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {

    private Long id;

    private String name;

    private int completed;

    private Date deadline;

    @SerializedName("user_id")
    private Long userId;

    @SerializedName("project_id")
    private Long projectId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed ? 1 : 0;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDeadline(String deadlineString) throws ParseException {
        if (deadlineString != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.deadline = dateFormat.parse(deadlineString);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed == 1;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                ", deadline=" + deadline +
                ", userId=" + userId +
                ", projectId=" + projectId +
                '}';
    }
}
