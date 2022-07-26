package homework.command;

import homework.util.EnumStatus;
import homework.entity.task.Task;
import homework.entity.user.User;
import homework.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import static homework.util.MessageEnum.ERROR_RESULT;
import static homework.util.MessageEnum.STATUS_ERROR;

class PatchExecutorTest {
    PatchExecutor patchExecutor;
    @Mock
    private TaskService taskService;
    private String cmd="task#1;status#в_работе";
    private User user=new User();
    private Task task=new Task();

    public PatchExecutorTest() {
        user.setId(1L);
        task.setId(1L);
        task.setUser(user);
        task.setDate(Calendar.getInstance());
        task.setStatus(EnumStatus.EMPTY);
        MockitoAnnotations.openMocks(this);
        user.setTaskList(new ArrayList<>());
        user.addTask(task);
        patchExecutor=new PatchExecutor(taskService);
    }

    @Test
    void executeCmd_ChangeTaskStatus_ExpectedBehavior(){
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(task));
        String result = patchExecutor.executeCmd(cmd);
        Assertions.assertEquals(String.format("Статус задачи c id=%d был изменен на '%s'", task.getId()
                ,task.getStatus().getStatus()),result);
    }

    @Test
    void executeCmd_ChangeTaskStatus_NotFind_or_WrongValue() {
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(null));
        String result = patchExecutor.executeCmd(cmd);
        Assertions.assertEquals("Task c id=1 не существует",result);

        result = patchExecutor.executeCmd("task#123s,status#в_работе");
        Assertions.assertEquals(ERROR_RESULT.getMessage(),result);
    }

    @Test
    void executeCmd_ChangeTaskStatus_IncorrectStatus() {
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(task));
        String result = patchExecutor.executeCmd("task#1;status#вработе");
        Assertions.assertEquals(STATUS_ERROR.getMessage(),result);
    }
}