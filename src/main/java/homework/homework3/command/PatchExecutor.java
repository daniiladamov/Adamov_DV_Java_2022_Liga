package homework.homework3.command;

import homework.homework2.entity.EnumStatus;
import homework.homework2.entity.task.Task;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;

import java.util.Arrays;

public class PatchExecutor extends AbstractCommandExecutor {
    public PatchExecutor(String command, SimpleCache simpleCache) {
        super(command, simpleCache);
    }

    @Override
    public String executeCmd() {
        String result = "Параметры запроса заданы неверно";
        if (changeTaskStatus.matcher(command).matches()) {
            try {
                result = patchTaskStatus(command);
            } catch (MappingException e) {
                return e.getMessage();
            }
        }
        return result;
    }

    private String patchTaskStatus(String command) throws MappingException {
        Long id;
        String parse = command.replaceAll("task#", "")
                .replaceAll("status#", "");
        String[] split = parse.split(";");
        try {
            id = Long.parseLong(split[0]);
        } catch (NumberFormatException e) {
            throw new MappingException("в качестве id необходимо передавать число");
        }
        Task taskFind = simpleCache.getTask(id);
        if (taskFind == null)
            throw new MappingException(String.format("Task c id=%d не существует", id));
        else {
            EnumStatus status = Arrays.stream(EnumStatus.values()).filter(sts -> sts.getStatus()
                            .equalsIgnoreCase(split[1].replace("_", " ")))
                    .findFirst().orElse(null);
            if (status == null)
                throw new MappingException(statusError);
            else {
                taskFind.setStatus(status);
                return String.format("Статус задачи c id=%d был изменен на '%s'", id, status.getStatus());
            }
        }

    }
}
