package homework.controller;

import homework.entity.task.Task;
import homework.entity.task.TaskGetDto;
import homework.entity.task.TaskSaveDto;
import homework.entity.user.User;
import homework.entity.user.UserGetDto;
import homework.exception.EntityNotFoundException;
import homework.service.TaskService;
import homework.util.CustomPage;
import homework.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;
    private final DtoMapper dtoMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createTask(@Valid @RequestBody TaskSaveDto taskSaveDto){
        Task task=modelMapper.map(taskSaveDto, Task.class);
        return taskService.save(task);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskGetDto> getTasks(CustomPage customPage) {
        Page<Task> tasks = taskService.getTasks(customPage);
        return dtoMapper.mapToPage(tasks, TaskGetDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskGetDto getTask(@PathVariable Long id){
        Optional<Task> task = taskService.getTask(id);
        if (task.isPresent()) {
            TaskGetDto userGetDto = modelMapper.map(task.get(),TaskGetDto.class);
            return userGetDto;
        } else {
            throw new EntityNotFoundException(
                    String.format(exceptionMessage, Task.class.getSimpleName(), id));
        }
    }

    //@todo: put, delete mapping и т.д
}
