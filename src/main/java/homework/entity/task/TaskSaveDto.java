package homework.entity.task;

import homework.util.EnumStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TaskSaveDto {
    @NotBlank
    private String title;
    private EnumStatus status;
    private Date date=new Date();

}
