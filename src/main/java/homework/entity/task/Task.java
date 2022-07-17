package homework.entity.task;

import homework.entity.EnumStatus;
import homework.entity.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Getter
@Setter
@Entity
@Table(name="tasks")
@NoArgsConstructor
@EqualsAndHashCode
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name="status",columnDefinition = "varchar(255) default 'EMPTY'")
    private EnumStatus status;
    @Column(name="task_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Calendar date;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Override
    public String toString() {
        return "Task: " +
                "id=" + id +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", status=" + status.getStatus() +
//                ", date=" + date.format(DateTimeFormatter.ofPattern()) +
                ", date=" + new SimpleDateFormat("dd.MM.yyyy").format(date.getTime())
                + ", user=" + user.getName()
                ;
    }
}
