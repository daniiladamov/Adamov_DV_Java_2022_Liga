package homework.entity.user;

import homework.entity.task.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Task> taskList;

    public void addTask(Task task) {
        taskList.add(task);

    }

    public void removeTask(Task task) {
        if (!taskList.remove(task))
            System.err.println(String.format("У пользователя %s нет назначенной задачи %s", this, task));
    }

    @Override
    public String toString() {
        return "User:" +
                "id=" + id +
                ", name='" + name + '\''
                + ", tasks=" + taskList.stream().map(x -> "Task#" + x.getId()).collect(Collectors.toList());
    }
}
