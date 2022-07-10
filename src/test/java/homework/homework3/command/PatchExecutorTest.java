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
import static homework.homework3.util.MessageEnum.STATUS_ERROR;

class PatchExecutorTest {
    PatchExecutor patchExecutor;
    @Mock
    private SimpleCache simpleCache;
    private String cmd="task#1;status#в_работе";
    private User user=new User(1L,null);
    private Task task=new Task(1L,null,null,user, LocalDate.now());

    public PatchExecutorTest() {
        MockitoAnnotations.openMocks(this);
        user.addTask(task);
        patchExecutor=new PatchExecutor(simpleCache);
    }

    @Test
    void executeCmd_ChangeTaskStatus_ExpectedBehavior(){
        Mockito.when(simpleCache.getTask(1L)).thenReturn(task);
        String result = patchExecutor.executeCmd(cmd);
        Assertions.assertEquals(String.format("Статус задачи c id=%d был изменен на '%s'", task.getId()
                ,task.getStatus().getStatus()),result);
    }

    @Test
    void executeCmd_ChangeTaskStatus_NotFind_or_WrongValue() {
        Mockito.when(simpleCache.getTask(1L)).thenReturn(null);
        String result = patchExecutor.executeCmd(cmd);
        Assertions.assertEquals("Task c id=1 не существует",result);

        result = patchExecutor.executeCmd("task#123s,status#в_работе");
        Assertions.assertEquals(ERROR_RESULT.getMessage(),result);
    }

    @Test
    void executeCmd_ChangeTaskStatus_IncorrectStatus() {
        Mockito.when(simpleCache.getTask(1L)).thenReturn(task);
        String result = patchExecutor.executeCmd("task#1;status#вработе");
        Assertions.assertEquals(STATUS_ERROR.getMessage(),result);
    }
}