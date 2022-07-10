package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.service.SimpleCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static homework.homework3.util.PatternEnum.TASK_BY_ID;

@Component
@RequiredArgsConstructor
public class DeleteExecutor implements CommandExecutor{
    private final SimpleCache simpleCache;

    @Override
    public String executeCmd(String command) {
        String result = "Параметры запроса заданы неверно";
        if (TASK_BY_ID.getPattern().matcher(command).matches()) {
            Long id;
            try {
                id = Long.parseLong(command.replaceAll("task#", ""));
            } catch (NumberFormatException ex) {
                return "в качестве id необходимо передавать число";
            }
            Task task = simpleCache.getTask(id);
            if (task == null)
                return String.format("Task c id=%d не существует", id);
            else {
                simpleCache.removeTask(id);
                return String.format("Задача с id=%d была удалена.\n" + task, id);
            }

        }
        return result;
    }
}
