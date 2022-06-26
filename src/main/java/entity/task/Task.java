package entity.task;

import entity.EnumStatus;
import entity.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task {
    private static Long maxIdValue = 0L;
    private final Long id;
    private String title;
    private String description;
    private EnumStatus status = EnumStatus.EMPTY;
    private LocalDate date;
    private User user;

    public Task(Long id, String title, String description, User user, LocalDate date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
        this.date = date;
        if (id > maxIdValue)
            maxIdValue = id;
    }

    public static Long getGlobalId() {
        return ++maxIdValue;
    }

    public Long getId() {
        return id;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task: " +
                "id=" + id +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", status=" + status.getStatus() +
                ", date=" + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                ", user=" + user.getName();
    }

    public String toFileFormat() {
        List<String> resultList = new ArrayList<>(
                List.of(getId().toString(), getTitle(), getDescription(), getUser().getId().toString(),
                        getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        );
        if (!status.equals(EnumStatus.EMPTY))
            resultList.add(status.getStatus());
        return resultList.stream().collect(Collectors.joining(","));
    }
}
