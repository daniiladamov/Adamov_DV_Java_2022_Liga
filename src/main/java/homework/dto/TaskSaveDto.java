package homework.dto;

import homework.util.enums.EnumStatus;
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
