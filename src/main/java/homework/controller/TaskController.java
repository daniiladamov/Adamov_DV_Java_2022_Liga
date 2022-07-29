package homework.controller;

import homework.entity.comment.Comment;
import homework.entity.comment.CommentSaveDto;
import homework.entity.task.Task;
import homework.entity.task.TaskGetDto;
import homework.entity.task.TaskSaveDto;
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

import javax.validation.Valid;
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable Long id){
        relationService.removeTask(id);
        if(!taskService.removeTask(id))
            throw new EntityNotFoundException(String.format(exceptionMessage,Task.class.getSimpleName(),id));
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createCommentForTask(@PathVariable Long id, @Valid @RequestBody CommentSaveDto commentSaveDto)
    throws EntityNotFoundException{
        Comment comment = modelMapper.map(commentSaveDto, Comment.class);
        return relationService.createComment(comment,id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskGetDto updateTask(@PathVariable Long id, @RequestBody TaskSaveDto taskSaveDto){
        Task task=modelMapper.map(taskSaveDto,Task.class);
        task.setId(id);
        Task taskUpdate=relationService.updateTask(task);
        return modelMapper.map(taskUpdate,TaskGetDto.class);
    }
}
