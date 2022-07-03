package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import homework.homework2.service.mapper.TaskMapper;

public class PostExecutor extends AbstractCommandExecutor {
    private TaskMapper taskMapper;

    public PostExecutor(String command, SimpleCache simpleCache, TaskMapper taskMapper) {
        super(command, simpleCache);
        this.taskMapper = taskMapper;
    }

    @Override
    public String executeCmd() {
        String lineToParse = command.replaceAll("_", " ");
        try {
            taskMapper.setUpdate(false);
            Task task = taskMapper.mapToNewEntity(lineToParse);
            return "Создана Task c id=" + task.getId();
        } catch (MappingException e) {
            return e.getMessage();
        }
    }
}
