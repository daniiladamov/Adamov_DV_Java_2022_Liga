package homework.homework3.command;

import homework.homework2.entity.EnumStatus;
import homework.homework2.service.SimpleCache;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractCommandExecutor implements CommandExecutor {
    protected static String errorResult = "Неверно задан параметр запроса 'cmd'";
    protected static Pattern userTasks = Pattern.compile("userTasks#\\d+");
    protected static Pattern userTasksSort = Pattern.compile("userTasksSort#\\d+");
    protected static Pattern taskById = Pattern.compile("task#\\d+");
    protected static Pattern changeTaskStatus = Pattern.compile("task#\\d+;status#.+");
    protected static String statusError = "Статус может принимать значения:" + Arrays.stream(EnumStatus.values())
            .map(sts -> sts.getStatus().replace(" ", "_")).collect(Collectors.joining(","));
    protected String command;
    protected SimpleCache simpleCache;


}