package homework.controller;

import homework.entity.task.Task;
import homework.entity.task.TaskGetDto;
import homework.exception.EntityNotFoundException;
import homework.service.RelationService;
import homework.service.TaskService;
import homework.util.CustomPage;
import homework.util.DtoPageMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/tasks")
public class TaskController {
    private final TaskService taskService;
    private final RelationService relationService;
    private final ModelMapper modelMapper;
    private final DtoPageMapper dtoPageMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskGetDto> getTasks(CustomPage customPage) {
        Page<Task> tasks = taskService.getTasks(customPage);
        return dtoPageMapper.mapToPage(tasks, TaskGetDto.class);
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
//@todo:вслед за задачей должны удалиться все комменты, проверить + если user из project_user при условии изменения баланса
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskGetDto deleteTask(@PathVariable Long id){
        Optional<Task> taskOptional=taskService.getTask(id);
        if (taskOptional.isPresent()){
            return null; //@todo
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage, Task.class.getSimpleName(), id));
    }
}
