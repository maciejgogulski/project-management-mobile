package pl.kalisz.ak.pup.todolist_mobile.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Note implements Serializable {
    private Long id;

    @SerializedName("project_id")
    private Long projectId;

    @SerializedName("task_id")
    private Long taskId;

    private String content;

    @SerializedName("updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedAtString(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(updatedAt);

        String year = addZeroInFrontOfSingleDigit(calendar.get(Calendar.YEAR));
        String month = addZeroInFrontOfSingleDigit(calendar.get(Calendar.MONTH) + 1);
        String day = addZeroInFrontOfSingleDigit(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = addZeroInFrontOfSingleDigit(calendar.get(Calendar.HOUR_OF_DAY));
        String min = addZeroInFrontOfSingleDigit(calendar.get(Calendar.MINUTE));

        return year + "-" + month + "-" + day + " " + hour + ":" + min;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Dodanie zera przed jednocyfowymi elementami daty i godziny, aby pasowały do formatu.
     *
     * @param number Liczba do sparsowania.
     * @return Sparsowana liczba.
     */
    private String addZeroInFrontOfSingleDigit(int number) {
        return (number < 10) ? ("0" + number) : String.valueOf(number);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", taskId=" + taskId +
                ", content='" + content + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
