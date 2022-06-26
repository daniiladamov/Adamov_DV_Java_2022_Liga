package entity.user;

import entity.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private final Long id;
    private String name;
    private List<Task> taskList = new ArrayList<>();

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTask(Task task) {
        taskList.add(task);

    }

    public void removeTask(Task task) {
        if (!taskList.remove(task))
            System.err.println(String.format("У пользователя %s нет назначенной задачи %s", this, task));
    }

    public Long getId() {
        return id;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    @Override
    public String toString() {
        return "User:" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + taskList.stream().map(x -> "Task#" + x.getId()).collect(Collectors.toList());
    }
}
