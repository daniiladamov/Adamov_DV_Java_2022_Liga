package homework.controller;

import homework.entity.project.Project;
import homework.entity.project.ProjectGetDto;
import homework.entity.task.Task;
import homework.entity.task.TaskGetDto;
import homework.entity.user.User;
import homework.entity.user.UserGetDto;
import homework.entity.user.UserSaveDto;
import homework.exception.EntityNotFoundException;
import homework.service.RelationService;
import homework.service.UserService;
import homework.util.CustomPage;
import homework.util.DtoPageMapper;
import homework.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users")
public class UserController {
    private final UserValidator userValidator;
    private final DtoPageMapper dtoPageMapper;
    private final RelationService relationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/projects")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProjectGetDto> getProjects(@PathVariable Long id, CustomPage customPage){
        Page<Project> projects = relationService.getUserProjects(id, customPage);
        return dtoPageMapper.mapToPage(projects,ProjectGetDto.class);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskGetDto> getTasks(@PathVariable Long id, CustomPage customPage){
        Page<Task> tasks=relationService.getUserTasks(id,customPage);
        return dtoPageMapper.mapToPage(tasks,TaskGetDto.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserGetDto> getUsers(CustomPage customPage) {
        Page<User> users = userService.getUsers(customPage);
        return dtoPageMapper.mapToPage(users, UserGetDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@Validated @RequestBody UserSaveDto userSaveDto, BindingResult bindingResult) {
        userValidator.validate(userSaveDto,bindingResult);
        User user = modelMapper.map(userSaveDto, User.class);
        return userService.createUser(user);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public UserGetDto updateUser(@Validated @RequestBody UserSaveDto userSaveDto, @PathVariable Long id) {
        User user = modelMapper.map(userSaveDto, User.class);
        user.setId(id);
        User userUpdate = userService.updateUser(user);
        return modelMapper.map(userUpdate, UserGetDto.class);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        relationService.deleteUser(id);
    }
}
