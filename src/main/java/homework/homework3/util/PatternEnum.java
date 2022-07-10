package homework.homework3.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public enum PatternEnum {
    USER_TASKS(Pattern.compile("userTasks#\\d+")),
    USER_TASKS_SORT(Pattern.compile("userTasksSort#\\d+")),
    TASK_BY_ID(Pattern.compile("task#\\d+")),
    CHANGE_TASK_STATUS(Pattern.compile("task#\\d+;status#.+"));

    private final Pattern pattern;
}
