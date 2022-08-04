package homework.controller;

import homework.dto.request.UserSaveDto;
import homework.dto.response.JwtResponse;
import homework.dto.response.ProjectGetDto;
import homework.dto.response.TaskGetDto;
import homework.dto.response.UserGetDto;
import homework.entity.Project;
import homework.entity.Task;
import homework.entity.User;
import homework.mapper.ProjectMapper;
import homework.mapper.TaskMapper;
import homework.mapper.UserMapper;
import homework.service.JwtGeneratorService;
import homework.service.ProjectService;
import homework.service.TaskService;
import homework.service.UserService;
import homework.util.CustomPage;
import lombok.RequiredArgsConstructor;
import org.h2.security.auth.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final JwtGeneratorService jwtGeneratorService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    @GetMapping("/{id}/projects")
    public Page<ProjectGetDto> getProjects(@PathVariable Long id, Pageable pageable) {
        User user=userService.getUser(id);
        Page<Project> projects = projectService.getUserProjects(user, pageable);
        return projects.map(p -> projectMapper.toResponse(p));
    }
    @GetMapping("/{id}/tasks")
    public Page<TaskGetDto> getTasks(@PathVariable Long id, Pageable pageable) {
        User user=userService.getUser(id);
        Page<Task> tasks = taskService.getUserTasks(user,pageable);
        return tasks.map(t -> taskMapper.toResponse(t));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserGetDto> getUsers(CustomPage customPage) {
        Page<User> users = userService.getUsers(customPage);
        return users.map(u -> userMapper.toResponse(u));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createUser(@Validated @RequestBody UserSaveDto userSaveDto) throws AuthenticationException {
        User user = userMapper.toEntity(userSaveDto);
        userService.createUser(user);
        JwtResponse jwtResponse = jwtGeneratorService.generateTokens(user.getLogin());
        return jwtResponse;
    }
    @GetMapping("/{id}")
    public UserGetDto getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return userMapper.toResponse(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserGetDto updateUser(@Validated @RequestBody UserSaveDto userSaveDto, @PathVariable Long id) {
        User user = userMapper.toEntity(userSaveDto);
        user.setId(id);
        return userMapper.toResponse(userService.updateUser(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
