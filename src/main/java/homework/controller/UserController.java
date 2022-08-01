package homework.controller;

import homework.dto.*;
import homework.entity.Project;
import homework.entity.task.Task;
import homework.entity.user.User;
import homework.exception.EntityNotFoundException;
import homework.security.JwtGenerator;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;
    @GetMapping("/{id}/projects")
    public Page<ProjectGetDto> getProjects(@PathVariable Long id, CustomPage customPage) {
        Optional<User> userOptional=userService.getUser(id);
        Page<Project> projects = projectService.getUserProjects(userOptional,id, customPage);
        return projects.map(p -> modelMapper.map(p, ProjectGetDto.class));
    }
    @GetMapping("/{id}/tasks")
    public Page<TaskGetDto> getTasks(@PathVariable Long id, CustomPage customPage) {
        Optional<User> userOptional=userService.getUser(id);
        Page<Task> tasks = taskService.getUserTasks(userOptional,id, customPage);
        return tasks.map(t -> modelMapper.map(t, TaskGetDto.class));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserGetDto> getUsers(CustomPage customPage) {
        Page<User> users = userService.getUsers(customPage);
        return users.map(u -> modelMapper.map(u, UserGetDto.class));
    }

    @PostMapping("/refresh-jwt")
    public String refreshJwt(@Validated @RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken authToken=
                new UsernamePasswordAuthenticationToken(authDto.getPassword(),
                        authDto.getUsername());
        try {
            authenticationManager.authenticate(authToken);
        }
        catch (Exception ex){
            return "Аутентификация не пройдена";
        }
        return jwtGenerator.generateToken(authToken.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@Validated @RequestBody UserSaveDto userSaveDto){
        User user = modelMapper.map(userSaveDto, User.class);
        userService.createUser(user);
        return jwtGenerator.generateToken(user.getLogin());
    }
    @GetMapping("/{id}")
    public UserGetDto getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserGetDto.class);
        } else {
            throw new EntityNotFoundException(
                    String.format(exceptionMessage, User.class.getSimpleName(), id));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserGetDto updateUser(@Validated @RequestBody UserSaveDto userSaveDto, @PathVariable Long id) {
        User user = modelMapper.map(userSaveDto, User.class);
        user.setId(id);
        User userUpdate = userService.updateUser(user);
        return modelMapper.map(userUpdate, UserGetDto.class);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        Optional<User> userOptional=userService.getUser(id);
        userService.deleteUser(userOptional,id);
    }
}
