package homework.mapper;

import homework.entity.task.Task;
import homework.entity.EnumStatus;
import homework.entity.user.User;
import homework.exception.MappingException;
import homework.service.SimpleCache;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TaskMapper implements SimpleMapper<Task> {
    private SimpleCache simpleCache;
    private boolean update = false;

    public TaskMapper(SimpleCache simpleCache) {
        this.simpleCache = simpleCache;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    private final String dataRegex = "\\d{2}\\.\\d{2}\\.\\d{4}";
    private final Pattern pattern = Pattern.compile("\\d+,\\D+,.+,\\d+," + dataRegex + "(,\\D+)?"
            , Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public String mapToString(Task entity) {
        return entity.toFileFormat();
    }

    @Override
    public Task mapToEntity(String line) throws MappingException {
        if (line.isEmpty())
            return null;
        List<String> log = new ArrayList<>();
        log.add(String.format("Строка:\n " + line));
        String[] split = line.split(",");
        if (!pattern.matcher(line).matches())
            throw new MappingException(String.format("Строка '%s' не соответсвует заданном формату", line));

        Long id = null;
        try {
            final Long idTemp = Long.parseLong(split[0]);
            Task taskInCache = simpleCache.getTask(idTemp);
            if (taskInCache != null && !update)
                log.add(String.format("Задача с указанным id=%d уже создана:\n%s", idTemp, taskInCache));
            else{
                if (taskInCache==null&&update)
                    log.add("Не существует задачи с id="+idTemp);
                else
                    id = idTemp;
            }
        } catch (Exception ex) {
            log.add("Не удалось получить поле id из файла");
        }

        String title = split[1];
        String description = split[2];

        Long userId = Long.parseLong(split[3]);
        User user = simpleCache.getUser(userId);
        if (user == null)
            log.add(String.format("Не существует записи сущности User с id=%d\n", userId));

        LocalDate date = null;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            date = LocalDate.parse(split[4], dateFormat);
        } catch (Exception ex) {
            log.add("Неверный формат даты");
        }

        Task task;
        EnumStatus status = null;
        if (split.length == 6) {
            status = Arrays.stream(EnumStatus.values()).filter(sts -> sts.getStatus().equalsIgnoreCase(split[5]))
                    .findFirst().orElse(null);
            if (status == null)
                log.add("Статус задачи не соответствует заданному формату");
        }
        if (log.size() == 1) {
            task = new Task(id, title, description, user, date);
            if (status != null)
                task.setStatus(status);
            user.addTask(task);
        } else {
            String s = log.stream().collect(Collectors.joining("\n"));
            throw new MappingException(s);
        }
        update = false;
        simpleCache.addTask(task);
        return task;
    }

    public Task mapToNewEntity(String line) throws MappingException {
        String stringEntityFormat = Task.getGlobalId().toString() + "," + line;
        return mapToEntity(stringEntityFormat);
    }
}
