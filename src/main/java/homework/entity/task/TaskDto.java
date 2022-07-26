package homework.entity.task;

import homework.util.EnumStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TaskDto {
    private Long id;
    private String title;
    private EnumStatus status;
    private Calendar date;
}
