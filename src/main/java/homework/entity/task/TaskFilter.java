package homework.entity.task;

import homework.util.EnumStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskFilter {
    private List<EnumStatus> enumStatuses;
    private String dateFrom;
    private String dateTo;
}
