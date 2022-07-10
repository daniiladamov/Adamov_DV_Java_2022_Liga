package homework.homework3.command;

import homework.homework2.entity.task.Task;
import homework.homework2.entity.user.User;
import homework.homework2.service.SimpleCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static homework.homework3.util.MessageEnum.ERROR_RESULT;

class DeleteExecutorTest {
    @Mock
    private SimpleCache simpleCache;
    private DeleteExecutor deleteExecutor;
    private String cmd="task#1";
    private User user=new User(1L,null);
    private Task task=new Task(1L,null,null,user, LocalDate.now());

    public DeleteExecutorTest() {
        MockitoAnnotations.openMocks(this);
        user.addTask(task);
        deleteExecutor=new DeleteExecutor(simpleCache);
    }

    @Test
    void executeCmd_DeleteUserTask_ExpectedBehavior() {
        Mockito.when(simpleCache.getTask(1L)).thenReturn(task);
        Mockito.doNothing().when(simpleCache).removeTask(task.getId());
        String result = deleteExecutor.executeCmd(cmd);
        Mockito.verify(simpleCache,Mockito.times(1)).removeTask(1L);
        Assertions.assertEquals(String.format("Задача с id=%d была удалена.\n" + task, task.getId()),result);
    }

    @Test
    void executeCmd_DeleteUserTask_NotFind_Or_WrongValue(){
        Mockito.when(simpleCache.getTask(1L)).thenReturn(null);
        String result=deleteExecutor.executeCmd(cmd);
        Assertions.assertEquals("Task c id=1 не существует",result);
        String wronngCommand="task#123g";
        result = deleteExecutor.executeCmd(wronngCommand);
        Assertions.assertEquals(ERROR_RESULT.getMessage(),result);
    }
}