package homework.command;

import homework.entity.EnumStatus;
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

class DeleteExecutorTest {
    @Mock
    private TaskService taskService;
    private DeleteExecutor deleteExecutor;
    private String cmd="task#1";
    private User user=new User();
    private Task task=new Task();

    public DeleteExecutorTest() {
        user.setId(1L);
        task.setId(1L);
        task.setUser(user);
        task.setDate(Calendar.getInstance());
        task.setStatus(EnumStatus.EMPTY);
        MockitoAnnotations.openMocks(this);
        user.setTaskList(new ArrayList<>());
        user.addTask(task);
        deleteExecutor=new DeleteExecutor(taskService);
    }

    @Test
    void executeCmd_DeleteUserTask_ExpectedBehavior() {
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(task));
        Mockito.doReturn(true).when(taskService).removeTask(task.getId());
        String result = deleteExecutor.executeCmd(cmd);
        Mockito.verify(taskService,Mockito.times(1)).removeTask(1L);
        Assertions.assertEquals(String.format("Задача с id=%d была удалена.\n" + task, task.getId()),result);
    }

    @Test
    void executeCmd_DeleteUserTask_NotFind_Or_WrongValue(){
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(null));
        String result=deleteExecutor.executeCmd(cmd);
        Assertions.assertEquals("Task c id=1 не существует",result);
        String wronngCommand="task#123g";
        result = deleteExecutor.executeCmd(wronngCommand);
        Assertions.assertEquals(ERROR_RESULT.getMessage(),result);
    }
}