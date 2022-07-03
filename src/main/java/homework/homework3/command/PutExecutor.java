package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import homework.homework2.service.mapper.TaskMapper;

public class PutExecutor extends AbstractCommandExecutor {
    private TaskMapper taskMapper;

    public PutExecutor(String command, SimpleCache simpleCache, TaskMapper taskMapper) {
        super(command, simpleCache);
        this.taskMapper = taskMapper;
    }

    @Override
    public String executeCmd() {
        String parse = command.replaceAll("_", " ");
        taskMapper.setUpdate(true);
        try {
            Task task = taskMapper.mapToEntity(parse);
            return "Задача обновлена:\n" + task;
        } catch (MappingException e) {
            return e.getMessage();
        }
    }
}
