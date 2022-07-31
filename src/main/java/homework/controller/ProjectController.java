package homework.controller;

import homework.entity.project.Project;
import homework.entity.project.ProjectGetDto;
import homework.entity.project.ProjectSaveDto;
import homework.entity.task.Task;
import homework.entity.task.TaskGetDto;
import homework.entity.task.TaskSaveDto;
import homework.entity.user.User;
import homework.entity.user.UserGetDto;
import homework.exception.EntityNotFoundException;
import homework.service.ProjectService;
import homework.service.RelationService;
import homework.util.CustomPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final RelationService relationService;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @GetMapping("/{id}/tasks")
    public Page<TaskGetDto> getTasks(@PathVariable Long id, CustomPage customPage) {
        Page<Task> tasks = relationService.getProjectTasks(id, customPage);
        return tasks.map(t -> modelMapper.map(t, TaskGetDto.class));
    }

    @GetMapping("/{id}/users")
    public Page<UserGetDto> getUsers(@PathVariable Long id, CustomPage customPage) {
        Page<User> users = relationService.getProjectUsers(id, customPage);
        return users.map(u -> modelMapper.map(u, UserGetDto.class));
    }

    @GetMapping
    public Page<ProjectGetDto> getProjects(CustomPage customPage) {
        Page<Project> projects = projectService.getProjects(customPage);
        return projects.map(p -> modelMapper.map(p, ProjectGetDto.class));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createProject(@Valid @RequestBody ProjectSaveDto projectSaveDto) {
        Project project = modelMapper.map(projectSaveDto, Project.class);
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    public ProjectGetDto getProject(@PathVariable Long id) throws EntityNotFoundException {
        Optional<Project> project = projectService.getProject(id);
        if (project.isPresent()) {
            return modelMapper.map(project.get(), ProjectGetDto.class);
        } else {
            throw new EntityNotFoundException(
                    String.format(exceptionMessage, Project.class.getSimpleName(), id));
        }
    }

    @PutMapping("/{id}")
    public ProjectGetDto updateProject(@Valid @RequestBody ProjectSaveDto projectSaveDto,
                                       @PathVariable Long id)
            throws EntityNotFoundException {
        Project project = modelMapper.map(projectSaveDto, Project.class);
        project.setId(id);
        Project projectUpdate = projectService.updateProject(project);
        return modelMapper.map(projectUpdate, ProjectGetDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id)
            throws EntityNotFoundException {
        relationService.deleteProject(id);
    }

    @PostMapping("/{id}/tasks")
    public Long addTask(@Valid @RequestBody TaskSaveDto taskSaveDto, @PathVariable Long id,
                        @RequestParam(name = "user-id") Long userId)
            throws EntityNotFoundException {
        Task task = modelMapper.map(taskSaveDto, Task.class);
        return relationService.createTask(task, userId, id);
    }

}
