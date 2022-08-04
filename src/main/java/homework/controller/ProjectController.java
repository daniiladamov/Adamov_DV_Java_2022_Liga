package homework.controller;

import homework.dto.request.ProjectSaveDto;
import homework.dto.request.TaskSaveDto;
import homework.dto.response.ProjectGetDto;
import homework.dto.response.TaskGetDto;
import homework.dto.response.UserGetDto;
import homework.entity.Project;
import homework.entity.Task;
import homework.entity.User;
import homework.exception.EntityNotFoundException;
import homework.mapper.ProjectMapper;
import homework.mapper.TaskMapper;
import homework.mapper.UserMapper;
import homework.service.ProjectService;
import homework.service.TaskService;
import homework.service.UserService;
import homework.util.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/projects")
@PreAuthorize("hasRole('ADMIN')")
public class ProjectController {
    private final UserService userService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    @GetMapping("/{id}/tasks")
    public Page<TaskGetDto> getTasks(@PathVariable Long id, Pageable pageable) {
        Page<Task> tasks = taskService.getProjectTasks(projectService.getProject(id), pageable);
        return tasks.map(t -> taskMapper.toResponse(t));

    }

    @GetMapping("/{id}/users")
    public Page<UserGetDto> getUsers(@PathVariable Long id, Pageable pageable) {
        Page<User> users = userService.getProjectUsers(projectService.getProject(id),id, pageable);
        return users.map(u -> userMapper.toResponse(u));
    }

    @GetMapping
    public Page<ProjectGetDto> getProjects(CustomPage customPage) {
        Page<Project> projects = projectService.getProjects(customPage);
        return projects.map(p -> projectMapper.toResponse(p));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createProject(@Valid @RequestBody ProjectSaveDto projectSaveDto) {
        Project project = projectMapper.toEntity(projectSaveDto);
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    public ProjectGetDto getProject(@PathVariable Long id) throws EntityNotFoundException {
        return projectMapper.toResponse(projectService.getProject(id));
    }

    @PutMapping("/{id}")
    public ProjectGetDto updateProject(@Valid @RequestBody ProjectSaveDto projectSaveDto,
                                       @PathVariable Long id)
            throws EntityNotFoundException {
        Project project = projectMapper.toEntity(projectSaveDto);
        project.setId(id);
        Project projectUpdate = projectService.updateProject(project);
        return projectMapper.toResponse(projectUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }

    @PostMapping("/{id}/tasks")
    public Long addTask(@Valid @RequestBody TaskSaveDto taskSaveDto, @PathVariable(name = "id") Long projectId,
                        @RequestParam(name = "user-id") Long userId)
            throws EntityNotFoundException {
        Task task = taskMapper.toEntity(taskSaveDto);
        User user = userService.getUser(userId);
        Project project=projectService.getProject(projectId);
        return taskService.createTask(task,user,project);
    }

}
