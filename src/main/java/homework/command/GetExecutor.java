package homework.command;

import homework.entity.task.Task;
import homework.entity.task.TaskComparator;
import homework.entity.user.User;
import homework.exception.MappingException;
import homework.service.TaskService;
import homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

import static homework.util.MessageEnum.ERROR_RESULT;
import static homework.util.PatternEnum.*;

@Component
@RequiredArgsConstructor
public class GetExecutor implements CommandExecutor {

    private final TaskService taskService;
    private final UserService userService;


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
        Optional<User> userFind = userService.getUser(id);
        if (userFind.isEmpty())
            throw new MappingException(String.format("User c id=%d не существует", id));
        else {
            User user=userFind.get();
            if (user.getTaskList().size() == 0)
                throw new MappingException(String.format("У User c id=%d нет назначенных задач"
                        , user.getId()));
            if (needSort)
                return user.getTaskList().stream().sorted(new TaskComparator()).map(x -> x.toString())
                        .collect(Collectors.joining("\n"));
            else
                return user.getTaskList().stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
        }
    }

    private String getTaskById(String command) throws MappingException {
        Long id;
        try {
            id = Long.parseLong(command.replaceAll("task#", ""));
        } catch (NumberFormatException ex) {
            throw new MappingException("в качестве id необходимо передавать число");
        }
        Optional<Task> task = taskService.getTask(id);
        if (task.isEmpty())
            throw new MappingException(String.format("Task c id=%d не существует", id));
        else
            return task.get().toString();
    }
}
