package homework.homework3.util;

import homework.homework2.entity.EnumStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Getter
public enum MessageEnum {
    ERROR_RESULT("Неверно задан параметр запроса 'cmd'"),
    STATUS_ERROR("Статус может принимать значения:" + Arrays.stream(EnumStatus.values())
            .map(sts -> sts.getStatus().replace(" ", "_")).collect(Collectors.joining(",")));

    private final String message;

}
