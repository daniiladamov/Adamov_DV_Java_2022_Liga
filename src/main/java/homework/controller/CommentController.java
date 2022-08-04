package homework.controller;

import homework.dto.request.CommentSaveDto;
import homework.dto.response.CommentGetDto;
import homework.entity.Comment;
import homework.mapper.CommentMapper;
import homework.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v2/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping
    public Page<CommentGetDto> getComments(Pageable page) {
        Page<Comment> comments = commentService.getComments(page);
        return comments.map(c -> commentMapper.toResponse(c));
    }

    @GetMapping("/{id}")
    public CommentGetDto getComment(@PathVariable Long id) {
        Comment comment = commentService.getComment(id);
        return  commentMapper.toResponse(comment);
    }

    @DeleteMapping("/{id}")
    public void removeComment(@PathVariable Long id) {
        commentService.remove(id);
    }

    @PutMapping("/{id}")
    public CommentGetDto updateComment(@PathVariable Long id,
                                       @Valid @RequestBody CommentSaveDto commentSaveDto) {
        Comment comment = commentMapper.toEntity(commentSaveDto);
        Comment updateComment = commentService.updateComment(id,comment);
        return commentMapper.toResponse(updateComment);
    }

}
