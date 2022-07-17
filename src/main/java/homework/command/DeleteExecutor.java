package homework.command;

import homework.entity.task.Task;
import homework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static homework.util.MessageEnum.ERROR_RESULT;
import static homework.util.PatternEnum.TASK_BY_ID;

@Component
@RequiredArgsConstructor
public class DeleteExecutor implements CommandExecutor{
    private final TaskService taskService;

    @Override
    public String executeCmd(String command) {
        if (TASK_BY_ID.getPattern().matcher(command).matches()) {
            Long id;
            try {
                id = Long.parseLong(command.replaceAll("task#", ""));
            } catch (NumberFormatException ex) {
                return "в качестве id необходимо передавать число";
            }
            Optional<Task> task = taskService.getTask(id);
            if (task.isEmpty())
                return String.format("Task c id=%d не существует", id);
            else {
                boolean deleteFlag = taskService.removeTask(id);
                if (deleteFlag)
                    return String.format("Задача с id=%d была удалена.\n" + task.get(), id);
                else
                    return String.format("Задача с id=%d не была найдена.", id);
            }

        }
        return ERROR_RESULT.getMessage();
    }
}
