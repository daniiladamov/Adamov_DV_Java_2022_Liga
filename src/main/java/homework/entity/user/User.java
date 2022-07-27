package homework.entity.user;

import com.sun.istack.NotNull;
import homework.entity.project.Project;
import homework.entity.task.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    @NotNull
    private String firstName;
    @Column(name = "last_name")
    @NotNull
    private String lastName;
    @Column(name = "surname")
    private String surname;
    @Column(name = "login")
    @NotNull
    private String login;
    @NotNull
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Task> taskList = new ArrayList<>();
    @ManyToMany(mappedBy = "users")
    @Fetch(FetchMode.SUBSELECT)
    private Set<Project> projects = new HashSet<>();

    public void addTask(Task task) {
        taskList.add(task);
    }
}
