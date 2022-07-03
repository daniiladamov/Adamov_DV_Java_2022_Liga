package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.entity.task.TaskComparator;
import homework.homework2.entity.user.User;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;

import java.util.stream.Collectors;

public class GetExecutor extends AbstractCommandExecutor {

    public GetExecutor(String command, SimpleCache simpleCache) {
        super(command, simpleCache);
    }

    @Override
    public String executeCmd() {
        if (userTasks.matcher(command).matches()) {
            try {
                return getUserTask(false);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        if (userTasksSort.matcher(command).matches()) {
            try {
                return getUserTask(true);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        if (taskById.matcher(command).matches()) {
            try {
                return getTaskById();
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        return errorResult;
    }

    private String getUserTask(boolean needSort) throws MappingException {
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
            throw new MappingException(String.format("У User c id=%d нет назначенных задач", id));
        else {
            if (needSort)
                return userFind.getTaskList().stream().sorted(new TaskComparator()).map(x -> x.toString())
                        .collect(Collectors.joining("\n"));
            else
                return userFind.getTaskList().stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
        }
    }

    private String getTaskById() throws MappingException {
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
