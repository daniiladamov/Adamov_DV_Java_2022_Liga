package homework.util;

import homework.entity.user.User;
import homework.entity.user.UserGetDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoMapper {
    private final ModelMapper modelMapper;

    public <T,R>Page<R> mapToPage(@NonNull Page<T> page, @NonNull Class<R> clazz) {
        List<R> list=page.getContent().stream().map(p->modelMapper.map(p,clazz)).collect(Collectors.toList());
       return new PageImpl<>(list,page.getPageable(), page.getTotalElements());
    }
}
