package homework.mapper;

import homework.dto.request.ProjectSaveDto;
import homework.dto.response.ProjectGetDto;
import homework.entity.Project;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectMapper {
    ProjectGetDto toResponse(Project project);
    Project toEntity(ProjectSaveDto projectSaveDto);
}
