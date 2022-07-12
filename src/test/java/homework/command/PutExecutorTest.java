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

import java.time.LocalDate;

class PutExecutorTest {
    @Mock
    private TaskMapper taskMapper;
    private PutExecutor putExecutor;
    private User user=new User(1L,null);
    private Task task=new Task(1L,null,null,user, LocalDate.now());
    String cmd="1,Задание,Описание Задания,1,10.07.2022";
    private MappingException mappingException=new MappingException("исключение");

    public PutExecutorTest() {
        MockitoAnnotations.openMocks(this);
        putExecutor=new PutExecutor(taskMapper);
    }

    @Test
    void executeCmd_UpdateTask_ExpectedBehavior() throws MappingException {
        Mockito.when(taskMapper.mapToEntity(cmd)).thenReturn(task);
        String result = putExecutor.executeCmd(cmd);
        Assertions.assertEquals("Задача обновлена:\n" + task,result);
    }

    @Test
    void executeCmd_UpdateTask_With_WrongArguments() throws MappingException {
        Mockito.when(taskMapper.mapToEntity(cmd)).thenThrow(mappingException);
        String result = putExecutor.executeCmd(cmd);
        Assertions.assertEquals(mappingException.getMessage(),result);

    }
}