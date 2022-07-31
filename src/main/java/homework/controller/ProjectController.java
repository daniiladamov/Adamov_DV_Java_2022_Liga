package homework.controller;

import homework.entity.Project;
import homework.dto.ProjectGetDto;
import homework.dto.ProjectSaveDto;
import homework.entity.task.Task;
import homework.dto.TaskGetDto;
import homework.dto.TaskSaveDto;
import homework.entity.user.User;
import homework.dto.UserGetDto;
import homework.exception.EntityNotFoundException;
import homework.service.ProjectService;
import homework.service.TaskService;
import homework.service.UserService;
import homework.util.CustomPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/projects")
@PreAuthorize("hasRole('ADMIN')")
public class ProjectController {
    private final UserService userService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @GetMapping("/{id}/tasks")
    public Page<TaskGetDto> getTasks(@PathVariable Long id, CustomPage customPage) {
        Optional<Project> projectOptional=projectService.getProject(id);
        Page<Task> tasks = taskService.getProjectTasks(projectOptional,id, customPage);
        return tasks.map(t -> modelMapper.map(t, TaskGetDto.class));
    }

    @GetMapping("/{id}/users")
    public Page<UserGetDto> getUsers(@PathVariable Long id, CustomPage customPage) {
        Optional<Project> projectOptional=projectService.getProject(id);
        Page<User> users = userService.getProjectUsers(projectOptional,id, customPage);
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
        Optional<Project> projectOptional=projectService.getProject(id);
        projectService.deleteProject(projectOptional,id);
    }

    @PostMapping("/{id}/tasks")
    public Long addTask(@Valid @RequestBody TaskSaveDto taskSaveDto, @PathVariable(name = "id") Long projectId,
                        @RequestParam(name = "user-id") Long userId)
            throws EntityNotFoundException {
        Task task = modelMapper.map(taskSaveDto, Task.class);
        Optional<User> userOptional = userService.getUser(userId);
        Optional<Project> projectOptional=projectService.getProject(projectId);
        return taskService.createTask(task,userOptional,projectOptional, userId,projectId);
    }

}
