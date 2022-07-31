package homework.service;

import homework.entity.Comment;
import homework.entity.task.Task;
import homework.exception.EntityNotFoundException;
import homework.repository.CommentRepo;
import homework.util.CustomPage;
import homework.util.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepo commentRepo;
    @Value("${exception_message}")
    private String exceptionMessage;

    public Page<Comment> getComments(CustomPage customPage) {
        Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
        Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
        return commentRepo.findAll(pageable);
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepo.findById(id);
    }

    private Comment create(Comment comment) {
        return commentRepo.save(comment);

    }
    @Transactional
    public void remove(Long id) {
        Optional<Comment> commentOptional=commentRepo.findById(id);
        if (commentOptional.isPresent()){
            commentRepo.delete(commentOptional.get());
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage, Task.class.getSimpleName(),id));

    }

    private Page<Comment> getCommentsByTask(Task task, Pageable pageable) {
        return commentRepo.findAll(Specifications.getTaskComments(task),pageable);
    }
    @Transactional
    public Long createComment(Comment comment, Optional<Task> taskOptional, Long taskId) {
        if (taskOptional.isPresent()){
            comment.setTask(taskOptional.get());
            return create(comment).getId();
        }
        else
            throw new EntityNotFoundException(String.format(exceptionMessage,Task.class.getSimpleName(),taskId));
    }

    @Transactional
    public Comment updateComment(Optional<Comment> commentOptional, Comment comment) {
        if (commentOptional.isPresent()){
            Comment commentInBd=commentOptional.get();
            comment.setTask(commentInBd.getTask());
            return create(comment);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Comment.class.getSimpleName(),comment.getId()));
    }

    public Page<Comment> getTaskComments(Optional<Task> taskOptional, Long id, CustomPage customPage) {
        if (taskOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return getCommentsByTask(taskOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Task.class.getSimpleName(), id));
    }
}
