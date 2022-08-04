package homework.mapper;

import homework.dto.request.CommentSaveDto;
import homework.dto.response.CommentGetDto;
import homework.entity.Comment;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    CommentGetDto toResponse(Comment c);

    Comment toEntity(CommentSaveDto commentSaveDto);
}
