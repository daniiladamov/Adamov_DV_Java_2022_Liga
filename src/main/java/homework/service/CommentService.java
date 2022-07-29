package homework.service;

import homework.entity.comment.Comment;
import homework.repository.CommentRepo;
import homework.util.CustomPage;
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

    public Page<Comment> getComments(CustomPage customPage) {
        Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
        Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
        return commentRepo.findAll(pageable);
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepo.findById(id);
    }
}
