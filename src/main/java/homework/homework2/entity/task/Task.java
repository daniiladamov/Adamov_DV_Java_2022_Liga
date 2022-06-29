package homework.homework2.entity.task;

import homework.homework2.entity.EnumStatus;
import homework.homework2.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
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
