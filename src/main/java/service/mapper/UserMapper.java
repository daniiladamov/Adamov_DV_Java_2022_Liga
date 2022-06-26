package service.mapper;

import entity.user.User;
import exception.MappingException;
import service.SimpleCache;

import java.util.regex.Pattern;

public class UserMapper implements SimpleMapper<User> {

    SimpleCache simpleCache;

    public UserMapper(SimpleCache simpleCache) {
        this.simpleCache = simpleCache;
    }

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
            throw new MappingException(String.format("формат строки не соответсвует требуемуму по ТЗ\n%s ", line));
        String[] data = line.split(",");
        Long id = Long.parseLong(data[0]);
        String name = data[1];
        User user = new User(id, name);
        simpleCache.addUser(user);
        return user;
    }
}
