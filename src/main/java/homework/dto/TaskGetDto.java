package homework.dto;

import homework.util.enums.EnumStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class TaskGetDto {
    private Long id;
    private String title;
    private EnumStatus status;
    private Calendar date;
}
