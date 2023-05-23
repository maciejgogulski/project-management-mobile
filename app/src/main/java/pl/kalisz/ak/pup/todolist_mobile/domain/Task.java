package pl.kalisz.ak.pup.todolist_mobile.domain;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String name;

    private int completed;

    private Date deadline;

    public void setName(String name) {
        this.name = name;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed ? 1 : 0;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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
}
