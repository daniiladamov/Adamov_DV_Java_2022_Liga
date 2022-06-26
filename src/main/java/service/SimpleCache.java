package service;

import entity.task.Task;
import entity.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс хранит закэшированные данный при маппинге файла, с сохранением ссылки на ообъект сущности.
 * Соответсвующий кэш сбрасывается при удалении записи из файла, и обновляется при добавлении записи в файл
 */
public class SimpleCache {

    private static Map<Long, User> userCache = new HashMap<>();
    private static Map<Long, Task> taskCache = new HashMap<>();

    public void addUser(User user) {
        userCache.put(user.getId(), user);
    }

    public void addTask(Task task) {
        Task taskInCache = taskCache.get(task.getId());
        if (taskInCache != null) {
            User currentUser = taskInCache.getUser();
            if (task.getUser().getId() != currentUser.getId()) {
                currentUser.getTaskList().remove(taskInCache);
            }
        }
        taskCache.put(task.getId(), task);
    }

    public Task getTask(Long id) {
        return taskCache.get(id);
    }

    public User getUser(Long id) {
        return userCache.get(id);
    }

    public Collection<Task> getTasks() {
        return taskCache.values();
    }

    public Collection<User> getUsers() {
        return userCache.values();
    }

    public void removeTask(Long id) {
        Task removedTask = taskCache.remove(id);
        removedTask.getUser().getTaskList().remove(removedTask);
    }

    public void removeCache() {
        userCache.clear();
        taskCache.clear();
    }
}
