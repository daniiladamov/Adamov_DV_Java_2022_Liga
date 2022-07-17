package homework.command;

import homework.entity.EnumStatus;
import homework.entity.task.Task;
import homework.exception.MappingException;
import homework.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

import static homework.util.MessageEnum.ERROR_RESULT;
import static homework.util.MessageEnum.STATUS_ERROR;
import static homework.util.PatternEnum.CHANGE_TASK_STATUS;

@Component
@RequiredArgsConstructor
public class PatchExecutor implements CommandExecutor {
    private final TaskService taskService;
    @Override
    public String executeCmd(String command) {
        String result = ERROR_RESULT.getMessage();
        if (CHANGE_TASK_STATUS.getPattern().matcher(command).matches()) {
            try {
                result = patchTaskStatus(command);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        return result;
    }
    private String patchTaskStatus(String command) throws MappingException {
        Long id;
        String parse = command.replaceAll("task#", "")
                .replaceAll("status#", "");
        String[] split = parse.split(";");
        try {
            id = Long.parseLong(split[0]);
        } catch (NumberFormatException e) {
            throw new MappingException("в качестве id необходимо передавать число");
        }
        Optional<Task> taskFind = taskService.getTask(id);
        if (taskFind.isEmpty())
            throw new MappingException(String.format("Task c id=%d не существует", id));
        else {
            Task task=taskFind.get();
            EnumStatus status = Arrays.stream(EnumStatus.values()).filter(sts -> sts.getStatus()
                            .equalsIgnoreCase(split[1].replace("_", " ")))
                    .findFirst().orElse(null);
            if (status == null)
                throw new MappingException(STATUS_ERROR.getMessage());
            else {
                task.setStatus(status);
                taskService.save(task);
                return String.format("Статус задачи c id=%d был изменен на '%s'", id, status.getStatus());
            }
        }

    }
}
