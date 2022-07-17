package homework.command;

import homework.entity.task.Task;
import homework.exception.MappingException;
import homework.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class PostExecutor implements CommandExecutor {
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public String executeCmd(String command) {
        String lineToParse = command.replaceAll("_", " ");
        try {
            Task task = taskMapper.mapToNewEntity(lineToParse);
            return "Создана Task c id=" + task.getId();
        } catch (MappingException e) {
            return e.getMessage();
        }
    }
}
