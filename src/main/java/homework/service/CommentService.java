package homework.service;

import homework.entity.Comment;
import homework.entity.Task;
import homework.exception.EntityNotFoundException;
import homework.repository.CommentRepo;
import homework.util.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Comment> getComments(Pageable pageable){
        return commentRepo.findAll(pageable);
    }

    public Comment getComment(Long id) {
        return commentRepo.findById(id).orElseThrow(()->
                new EntityNotFoundException(String.format(exceptionMessage, Comment.class.getSimpleName(), id)));
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
    public Long createComment(Comment comment, Task task) {
            comment.setTask(task);
            return create(comment).getId();
    }

    @Transactional
    public Comment updateComment(Long id, Comment commentUpdate) {
        Comment comment=getComment(commentUpdate.getId());
            commentUpdate.setTask(comment.getTask());
            commentUpdate.setId(id);
            return create(commentUpdate);
    }

    public Page<Comment> getTaskComments(Task task, Pageable pageable) {
            return getCommentsByTask(task,pageable);
    }
}
