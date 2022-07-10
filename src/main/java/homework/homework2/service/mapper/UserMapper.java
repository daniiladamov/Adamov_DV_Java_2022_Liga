package homework.homework2.service.mapper;

import homework.homework2.entity.user.User;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserMapper implements SimpleMapper<User> {

    private final SimpleCache simpleCache;

    private final Pattern pattern = Pattern.compile("\\d+,[а-я]+", Pattern.UNICODE_CASE |
            Pattern.CASE_INSENSITIVE);

    @Override
    public String mapToString(User entity) {
        return entity.getId() + "," + entity.getName();
    }

    @Override
    public User mapToEntity(String line) throws MappingException {
        if (line.isEmpty())
            return null;
        if (!pattern.matcher(line).matches())
            throw new MappingException(String.format("Формат строки не соответсвует требуемуму\n%s ", line));
        String[] data = line.split(",");
        Long id = Long.parseLong(data[0]);
        String name = data[1];
        if (simpleCache.getUser(id)!=null)
            throw new MappingException(String.format("Пользователь с id=%d уже создан",id));
        User user = new User(id, name);
        simpleCache.addUser(user);
        return user;
    }
}
