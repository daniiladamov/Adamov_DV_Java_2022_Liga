package homework.command;

import homework.entity.task.Task;
import homework.exception.MappingException;
import homework.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class PutExecutor implements CommandExecutor{
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public String executeCmd(String command) {
        String parse = command.replaceAll("_", " ");
        try {
            Task task = taskMapper.mapToEntity(parse);
            return "Задача обновлена:\n" + task;
        } catch (MappingException e) {
            return e.getMessage();
        }
    }
}
