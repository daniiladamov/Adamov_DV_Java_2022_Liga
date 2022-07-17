package homework.service;

import homework.entity.EnumStatus;
import homework.entity.task.Task;
import homework.repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;

    public Optional<Task> getTask(Long id) {
        return taskRepo.findById(id);
    }

    public boolean removeTask(Long id) {
        Optional<Task> task = getTask(id);
        if (task.isPresent()){
            taskRepo.delete(task.get());
            return true;
        }
        else
            return false;

    }

    public void save(Task task) {
        if (task.getStatus()==null)
            task.setStatus(EnumStatus.EMPTY);
        taskRepo.save(task);
    }
}
