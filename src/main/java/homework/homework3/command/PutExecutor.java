package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.exception.MappingException;
import homework.homework2.service.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PutExecutor implements CommandExecutor{
    private final TaskMapper taskMapper;

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
