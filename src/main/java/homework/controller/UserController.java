package homework.controller;

import homework.entity.project.Project;
import homework.entity.project.ProjectGetDto;
import homework.entity.task.Task;
import homework.entity.task.TaskGetDto;
import homework.entity.user.User;
import homework.entity.user.UserGetDto;
import homework.entity.user.UserSaveDto;
import homework.exception.EntityNotFoundException;
import homework.exception.LoginAlreadyUsedException;
import homework.service.RelationService;
import homework.service.UserService;
import homework.util.CustomPage;
import homework.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final UserValidator userValidator;
    private final RelationService relationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @GetMapping("/{id}/projects")
    public Page<ProjectGetDto> getProjects(@PathVariable Long id, CustomPage customPage) {
        Page<Project> projects = relationService.getUserProjects(id, customPage);
        return projects.map(p -> modelMapper.map(p, ProjectGetDto.class));
    }

    @GetMapping("/{id}/tasks")
    public Page<TaskGetDto> getTasks(@PathVariable Long id, CustomPage customPage) {
        Page<Task> tasks = relationService.getUserTasks(id, customPage);
        return tasks.map(t -> modelMapper.map(t, TaskGetDto.class));
    }

    @GetMapping
    public Page<UserGetDto> getUsers(CustomPage customPage) {
        Page<User> users = userService.getUsers(customPage);
        return users.map(u -> modelMapper.map(u, UserGetDto.class));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@Valid @RequestBody UserSaveDto userSaveDto, BindingResult bindingResult)
            throws LoginAlreadyUsedException {
        userValidator.validate(userSaveDto, bindingResult);
        User user = modelMapper.map(userSaveDto, User.class);
        return userService.createUser(user);
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
    public UserGetDto updateUser(@Valid @RequestBody UserSaveDto userSaveDto, @PathVariable Long id) {
        User user = modelMapper.map(userSaveDto, User.class);
        user.setId(id);
        User userUpdate = userService.updateUser(user);
        return modelMapper.map(userUpdate, UserGetDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        relationService.deleteUser(id);
    }
}
