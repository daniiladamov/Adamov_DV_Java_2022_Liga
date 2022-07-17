package homework.service;

import com.sun.istack.NotNull;
import homework.entity.EnumStatus;
import homework.entity.task.Task;
import homework.repository.TaskRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;

    public Optional<Task> getTask(@NonNull Long id) {
        return taskRepo.findById(id);
    }

    @Transactional
    public boolean removeTask(@NonNull Long id) {
        Optional<Task> task = getTask(id);
        if (task.isPresent()){
            taskRepo.delete(task.get());
            return true;
        }
        else
            return false;

    }

    @Transactional
    public void save(@NotNull Task task) {
        if (task.getStatus()==null)
            task.setStatus(EnumStatus.EMPTY);
        taskRepo.save(task);
    }
}
