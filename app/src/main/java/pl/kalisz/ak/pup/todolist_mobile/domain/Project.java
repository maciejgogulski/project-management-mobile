package pl.kalisz.ak.pup.todolist_mobile.domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Project implements Serializable {

    private long id;

    private String name;

    private List<Task> tasks;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
