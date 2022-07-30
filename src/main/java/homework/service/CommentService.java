package homework.service;

import homework.entity.comment.Comment;
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

    @Transactional
    public Comment create(Comment comment) {
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

    public Page<Comment> getComemntsByTask(Task task, Pageable pageable) {
        return commentRepo.findAll(Specifications.getTaskComments(task),pageable);
    }
}
