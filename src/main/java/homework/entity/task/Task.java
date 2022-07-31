package homework.entity.task;

import homework.entity.Comment;
import homework.entity.Project;
import homework.entity.user.User;
import homework.util.enums.EnumStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="tasks")
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    @NotNull
    private String title;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name="status",columnDefinition = "varchar(255) default 'EMPTY'")
    private EnumStatus status;
    @Column(name="task_date", columnDefinition = "date default current_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @NotNull
    private User user;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name="project_id",referencedColumnName = "id")
    @NotNull
    private Project project;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Comment> comment=new HashSet<>();

    @Override
    public String toString() {
        return "Task: " +
                "id=" + id +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", status=" + status.getStatus() +
                ", date=" + new SimpleDateFormat("dd.MM.yyyy").format(date.getTime())
                + ", user=" + user.getFirstName();
    }
}
