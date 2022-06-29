package homework.homework2.entity.user;

import homework.homework2.entity.task.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class User {
    private final Long id;
    private String name;
    private List<Task> taskList = new ArrayList<>();

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
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
                ", name='" + name + '\'' +
                ", tasks=" + taskList.stream().map(x -> "Task#" + x.getId()).collect(Collectors.toList());
    }
}
