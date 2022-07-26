package homework.mapper;

import homework.util.EnumStatus;
import homework.entity.task.Task;
import homework.entity.user.User;
import homework.exception.MappingException;
import homework.service.TaskService;
import homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskMapper implements SimpleMapper<Task> {
    private final TaskService taskService;
    private final UserService userService;
    private boolean update = true;

    public void setUpdate(boolean update) {
        this.update = update;
    }

    private final String dataRegex = "\\d{2}\\.\\d{2}\\.\\d{4}";
    private final Pattern patternUpd = Pattern.compile("\\d+,\\D+,.+,\\d+," + dataRegex + "(,\\D+)?"
            , Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private final Pattern patternNew = Pattern.compile("new,\\D+,.+,\\d+," + dataRegex + "(,\\D+)?"
            , Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public String mapToString(Task entity) {
        return entity.toString();
    }

    @Override
    public Task mapToEntity(String line) throws MappingException {
        if (line.isEmpty())
            return null;
        List<String> log = new ArrayList<>();
        log.add(String.format("Строка:\n " + line));
        String[] split = line.split(",");
        Long id;
        Task task=null;
        if (!update){
            if (!patternNew.matcher(line).matches())
                throw new MappingException(String.format("Строка '%s' не соответсвует заданном формату", line));
            else
                task=new Task();
        }
        else {
            if (!patternUpd.matcher(line).matches())
                throw new MappingException(String.format("Строка '%s' не соответсвует заданном формату", line));
            try {
                id = Long.parseLong(split[0]);
                Optional<Task> taskInCache = taskService.getTask(id);
                    if (taskInCache.isEmpty())
                        log.add("Не существует задачи с id=" + id);
                    else{

                        task=taskInCache.get();
                    }
            } catch (Exception ex) {
                log.add("Не удалось получить поле id из файла");
            }
        }

        String title = split[1];
        String description = split[2];

        Long userId = Long.parseLong(split[3]);
        Optional<User> user = userService.getUser(userId);
        if (user.isEmpty())
            log.add(String.format("Не существует записи сущности User с id=%d\n", userId));

        LocalDate date = null;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            date = LocalDate.parse(split[4], dateFormat);
        } catch (Exception ex) {
            log.add("Неверный формат даты");
        }
        EnumStatus status = null;
        if (split.length == 6) {
            status = Arrays.stream(EnumStatus.values()).filter(sts -> sts.getStatus().equalsIgnoreCase(split[5]))
                    .findFirst().orElse(null);
            if (status == null)
                log.add("Статус задачи не соответствует заданному формату");
        }
        if (log.size() == 1 && !update) {
            task.setTitle(title);
            task.setDescription(description);
            task.setUser(user.get());
            task.setDate(GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault())));
            if (status != null)
                task.setStatus(status);
            else
                task.setStatus(EnumStatus.EMPTY);
        } else if (log.size()>1){
            String s = log.stream().collect(Collectors.joining("\n"));
            throw new MappingException(s);
        }
        update = true;
        taskService.save(task);
        return task;
    }
    public Task mapToNewEntity(String line) throws MappingException {
        setUpdate(false);
        return mapToEntity(line);
    }
}