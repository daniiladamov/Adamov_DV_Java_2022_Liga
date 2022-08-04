package homework.mapper;

import homework.dto.request.TaskSaveDto;
import homework.dto.response.TaskGetDto;
import homework.entity.Task;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {
    TaskGetDto toResponse(Task t);

    Task toEntity(TaskSaveDto taskSaveDto);
}
