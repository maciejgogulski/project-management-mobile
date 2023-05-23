package pl.kalisz.ak.pup.todolist_mobile.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {

    private long id;

    private String name;

    private int completed;

    private Date deadline;

    private long userId;

    private long projectId;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", completed=" + completed +
                ", deadline=" + deadline +
                ", userId=" + userId +
                ", projectId=" + projectId +
                '}';
    }
}
