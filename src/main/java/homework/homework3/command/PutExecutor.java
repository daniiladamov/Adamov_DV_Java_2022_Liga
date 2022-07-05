package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import homework.homework2.service.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PutExecutor extends AbstractCommandExecutor {
    private final TaskMapper taskMapper;
    private final SimpleCache simpleCache;

    @Override
    public String executeCmd(String command) {
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
