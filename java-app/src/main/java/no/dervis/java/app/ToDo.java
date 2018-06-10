package no.dervis.java.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ToDo {

    private Integer id;

    private String title;

    private boolean done;

    private Date dueDateTime;

    public ToDo() {  }

    public ToDo(Integer id, String title, boolean done, Date dueDate) {
        this.id = id;
        this.title = title;
        this.done = done;
    }

    public ToDo(String title, boolean done, Date dueDateTime) {
        this.title = title;
        this.done = done;
        this.dueDateTime = dueDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(Date dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", done=" + done +
                ", dueDateTime=" + dueDateTime +
                '}';
    }
}

