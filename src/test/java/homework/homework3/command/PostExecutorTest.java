package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.entity.user.User;
import homework.homework2.exception.MappingException;
import homework.homework2.service.mapper.TaskMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

class PostExecutorTest {
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private PostExecutor postExecutor;
    private User user=new User(1L,null);
    private Task task=new Task(1L,null,null,user, LocalDate.now());
    private String cmd="Задание,Описание Задания,1,10.07.2022";
    private String acceptResult="Создана Task c id=" + task.getId();
    private MappingException mappingException=new MappingException("исключение");

    public PostExecutorTest() {
        MockitoAnnotations.openMocks(this);
        postExecutor=new PostExecutor(taskMapper);
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