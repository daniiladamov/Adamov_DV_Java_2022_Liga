package homework.entity.task;

import homework.entity.EnumStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private EnumStatus status;
    private Calendar date;
}
