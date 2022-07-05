package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import homework.homework2.service.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostExecutor extends AbstractCommandExecutor {
    private final TaskMapper taskMapper;
    private final SimpleCache simpleCache;

    @Override
    public String executeCmd(String command) {
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
