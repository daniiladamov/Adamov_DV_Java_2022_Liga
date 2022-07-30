package homework.controller;

import homework.entity.comment.Comment;
import homework.entity.comment.CommentGetDto;
import homework.entity.comment.CommentSaveDto;
import homework.exception.EntityNotFoundException;
import homework.service.CommentService;
import homework.service.RelationService;
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
@RequestMapping("/v2/comments")
@RequiredArgsConstructor
public class CommentController {
    private final RelationService relationService;
    private final CommentService commentService;
    private final DtoPageMapper dtoPageMapper;
    private final ModelMapper modelMapper;
    @Value("${exception_message}")
    private String exceptionMessage;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentGetDto> getComment(CustomPage customPage) {
        Page<Comment> comments = commentService.getComments(customPage);
        return dtoPageMapper.mapToPage(comments, CommentGetDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentGetDto getComment(@PathVariable Long id){
        Optional<Comment> comment = commentService.getComment(id);
        if (comment.isPresent()) {
            CommentGetDto commentGetDto = modelMapper.map(comment.get(),CommentGetDto.class);
            return commentGetDto;
        } else {
            throw new EntityNotFoundException(
                    String.format(exceptionMessage, Comment.class.getSimpleName(), id));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeComment(@PathVariable Long id){
        commentService.remove(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentGetDto updateComment(@PathVariable Long id,
                                       @Valid @RequestBody CommentSaveDto commentSaveDto){
        Comment comment=modelMapper.map(commentSaveDto,Comment.class);
        comment.setId(id);
        Comment updateComment=relationService.updateComment(comment);
        return modelMapper.map(updateComment,CommentGetDto.class);
    }

}
