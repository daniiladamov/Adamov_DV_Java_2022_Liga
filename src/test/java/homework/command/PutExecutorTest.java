package homework.command;

import homework.entity.EnumStatus;
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
import java.util.Calendar;

class PutExecutorTest {
    @Mock
    private TaskMapper taskMapper;
    private PutExecutor putExecutor;
    private User user=new User();
    private Task task=new Task();
    String cmd="1,Задание,Описание Задания,1,10.07.2022";
    private MappingException mappingException=new MappingException("исключение");

    public PutExecutorTest() {
        user.setId(1L);
        task.setId(1L);
        task.setUser(user);
        task.setDate(Calendar.getInstance());
        task.setStatus(EnumStatus.EMPTY);
        MockitoAnnotations.openMocks(this);
        user.setTaskList(new ArrayList<>());
        user.addTask(task);
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