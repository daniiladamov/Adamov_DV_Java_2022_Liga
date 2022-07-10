package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.entity.task.TaskComparator;
import homework.homework2.entity.user.User;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static homework.homework3.util.MessageEnum.ERROR_RESULT;
import static homework.homework3.util.PatternEnum.*;

@Component
@RequiredArgsConstructor
public class GetExecutor implements CommandExecutor {

    private final SimpleCache simpleCache;


    @Override
    public String executeCmd(String command) {
        if (USER_TASKS.getPattern().matcher(command).matches()) {
            try {
                return getUserTask(false, command);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        if (USER_TASKS_SORT.getPattern().matcher(command).matches()) {
            try {
                return getUserTask(true, command);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        if (TASK_BY_ID.getPattern().matcher(command).matches()) {
            try {
                return getTaskById(command);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        return ERROR_RESULT.getMessage();
    }

    private String getUserTask(boolean needSort, String command) throws MappingException {
        Long id;
        if (needSort) {
            try {
                id = Long.parseLong(command.replaceAll("userTasksSort#", ""));
            } catch (NumberFormatException ex) {
                throw new MappingException("в качестве id необходимо передавать число");
            }
        } else {
            try {
                id = Long.parseLong(command.replaceAll("userTasks#", ""));
            } catch (NumberFormatException ex) {
                throw new MappingException("в качестве id необходимо передавать число");
            }
        }
        User userFind = simpleCache.getUser(id);
        if (userFind == null)
            throw new MappingException(String.format("User c id=%d не существует", id));
        else if (userFind.getTaskList().size() == 0)
            throw new MappingException(String.format("У User c id=%d нет назначенных задач", userFind.getId()));
        else {
            if (needSort)
                return userFind.getTaskList().stream().sorted(new TaskComparator()).map(x -> x.toString())
                        .collect(Collectors.joining("\n"));
            else
                return userFind.getTaskList().stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
        }
    }

    private String getTaskById(String command) throws MappingException {
        Long id;
        try {
            id = Long.parseLong(command.replaceAll("task#", ""));
        } catch (NumberFormatException ex) {
            throw new MappingException("в качестве id необходимо передавать число");
        }
        Task task = simpleCache.getTask(id);
        if (task == null)
            throw new MappingException(String.format("Task c id=%d не существует", id));
        else
            return task.toString();
    }
}
