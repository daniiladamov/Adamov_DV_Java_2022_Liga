package homework.controller;

import homework.dto.request.CommentSaveDto;
import homework.dto.request.TaskSaveDto;
import homework.dto.response.CommentGetDto;
import homework.dto.response.TaskGetDto;
import homework.entity.Comment;
import homework.entity.Task;
import homework.mapper.CommentMapper;
import homework.mapper.TaskMapper;
import homework.service.CommentService;
import homework.service.TaskService;
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
@RequestMapping("/v2/tasks")
public class TaskController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}/comments")
    public Page<CommentGetDto> getComments(@PathVariable Long id, Pageable pageable) {
        Task task=taskService.getTask(id);
        Page<Comment> comments = commentService.getTaskComments(task,pageable);
        return comments.map(c -> commentMapper.toResponse(c));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TaskGetDto> getTasks(CustomPage customPage) {
        Page<Task> tasks = taskService.getTasks(customPage);
        return tasks.map(t -> taskMapper.toResponse(t));
    }

    @GetMapping("/{id}")
    public TaskGetDto getTask(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        return taskMapper.toResponse(task);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTask(@PathVariable Long id) {
        taskService.removeTask(id);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createCommentForTask(@PathVariable Long id, @Valid @RequestBody CommentSaveDto commentSaveDto) {
        Comment comment = commentMapper.toEntity(commentSaveDto);
        Task task=taskService.getTask(id);
        return commentService.createComment(comment,task);
    }

    @PutMapping("/{id}")
    public TaskGetDto updateTask(@PathVariable Long id, @RequestBody TaskSaveDto taskSaveDto) {
        Task task = taskMapper.toEntity(taskSaveDto);
        return taskMapper.toResponse(taskService.updateTask(id,task));
    }
}
