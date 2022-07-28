package homework.entity.task;

import homework.util.EnumStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;
@Getter
@Setter
@NoArgsConstructor
public class TaskSaveDto {
    @NotBlank
    private String title;
    private EnumStatus status=null;
    private Calendar date=Calendar.getInstance();
}
