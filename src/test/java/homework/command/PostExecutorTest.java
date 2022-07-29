package homework.command;

import homework.entity.task.Task;
import homework.entity.user.User;
import homework.exception.MappingException;
import homework.mapper.TaskMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;

class PostExecutorTest {
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private PostExecutor postExecutor;
    private User user=new User();
    private Task task=new Task();
    private String cmd="Задание,Описание Задания,1,10.07.2022";
    private String acceptResult;
    private MappingException mappingException=new MappingException("исключение");

    public PostExecutorTest() {
        user.setId(1L);
        task.setId(1L);
        task.setUser(user);
        task.setDate(new Date());
        MockitoAnnotations.openMocks(this);
        user.setTaskList(new ArrayList<>());
        user.addTask(task);
        postExecutor=new PostExecutor(taskMapper);
        acceptResult="Создана Task c id=" + task.getId();
    }

    @Test
    void executeCmd_creteTask_ExpectedBehavior() throws MappingException {
        Mockito.when(taskMapper.mapToNewEntity(cmd)).thenReturn(task);
        String result = postExecutor.executeCmd(cmd);
        Mockito.verify(taskMapper,Mockito.times(1)).mapToNewEntity(cmd);
        Assertions.assertEquals(acceptResult,result);
    }

    @Test
    void executeCmd_CreteTask_With_WrongArguments() throws MappingException {
        Mockito.when(taskMapper.mapToNewEntity(cmd)).thenThrow(mappingException);
        String result = postExecutor.executeCmd(cmd);
        Assertions.assertEquals(mappingException.getMessage(),result);

    }
}