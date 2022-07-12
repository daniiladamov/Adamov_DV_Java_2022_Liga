package homework.mapper;

import homework.entity.user.User;
import homework.exception.MappingException;
import homework.service.SimpleCache;
import homework.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class UserMapperTest {
    @Mock
    private SimpleCache simpleCache;
    private UserMapper userMapper;
    private String line="1,Иван";
    private User user=new User(1L,"Иван");

    public UserMapperTest() {
        MockitoAnnotations.openMocks(this);
        userMapper=new UserMapper(simpleCache);
    }

    @Test
    void mapToEntity_createUser_ExpectedBehavior() throws MappingException {
        User createdUser = userMapper.mapToEntity(line);
        Assertions.assertEquals(user.getId(),createdUser.getId());
        Assertions.assertEquals(user.getName(),createdUser.getName());
        Assertions.assertTrue(user.getTaskList().size()==createdUser.getTaskList().size());
        Mockito.verify(simpleCache,Mockito.times(1)).addUser(createdUser);
    }

    @Test
    void mapToEntity_createUser_From_IncorrectLine() throws MappingException {
        User createdUser = userMapper.mapToEntity("");
        Assertions.assertNull(createdUser);
        String incorrectLine="F,Иван";
        Throwable throwable=Assertions.assertThrows(MappingException.class,()->userMapper.mapToEntity(incorrectLine));
        Assertions.assertEquals(String.format("Формат строки не соответсвует требуемуму\n%s ", incorrectLine),
                throwable.getMessage());
    }

    @Test
    void mapToEntity_createUser_With_IncorrectId() {
        Mockito.when(simpleCache.getUser(1L)).thenReturn(user);
        Throwable throwable=Assertions.assertThrows(MappingException.class,()->userMapper.mapToEntity(line));
        Assertions.assertEquals(String.format("Пользователь с id=%d уже создан",user.getId()),throwable.getMessage());

    }
}