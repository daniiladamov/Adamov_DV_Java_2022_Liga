package homework.mapper;

import homework.dto.request.UserSaveDto;
import homework.dto.response.UserGetDto;
import homework.dto.security.UserAppDto;
import homework.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserGetDto toResponse(User u);

    User toEntity(UserSaveDto userSaveDto);

    UserAppDto toAppEntity(User user);
}
