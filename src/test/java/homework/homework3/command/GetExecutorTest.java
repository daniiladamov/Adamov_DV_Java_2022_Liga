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

class GetExecutorTest {
    @Mock
    private SimpleCache simpleCache;
    private String cmdGetUserTasks="userTasks#1";
    private String cmdGetUserTasksSort="userTasksSort#1";
    private String cmdTaskById="task#1";
    private GetExecutor getExecutor;
    private User user=new User(1L,null);
    private Task task=new Task(1L,null,null,user, LocalDate.now());

    public GetExecutorTest() {
        MockitoAnnotations.openMocks(this);
        user.addTask(task);
        getExecutor=new GetExecutor(simpleCache);
    }

    @Test
    void executeCmd_GetUserTasks_ExpectedBehavior() {
        Mockito.when(simpleCache.getUser(1L)).thenReturn(user);
        String result = getExecutor.executeCmd(cmdGetUserTasks);
        Assertions.assertEquals(user.getTaskList().get(0).toString(),result);
    }

    @Test
    void executeCmd_GetUserTasks_NotFind_Or_WrongValue() {
        String result = getExecutor.executeCmd(cmdGetUserTasks);
        String wrongCommand="userTasks#123k";
        String error=getExecutor.executeCmd(wrongCommand);

        Assertions.assertEquals("User c id=1 не существует",result);
        Assertions.assertEquals(ERROR_RESULT.getMessage(),error);
    }

    @Test
    void executeCmd_GetUserTasks_EmptyTaskList() {
        User userEmpty=new User(2L,null);
        Mockito.when(simpleCache.getUser(1L)).thenReturn(userEmpty);
        String result = getExecutor.executeCmd(cmdGetUserTasks);
        Assertions.assertEquals(String.format("У User c id=%d нет назначенных задач", userEmpty.getId()),result);
    }

    @Test
    void executeCmd_GetTaskById_ExpectedBehavior() {
        Mockito.when(simpleCache.getTask(1L)).thenReturn(task);
        String s = getExecutor.executeCmd(cmdTaskById);
        Assertions.assertEquals(task.toString(),s);
    }

    @Test
    void executeCmd_GetTaskById_NotFind_Or_WrongValue() {
        Mockito.when(simpleCache.getTask(1L)).thenReturn(null);
        String taskNotFind = getExecutor.executeCmd(cmdTaskById);
        String wrongCommand="task#123k";
        String error=getExecutor.executeCmd(wrongCommand);

        Assertions.assertEquals("Task c id=1 не существует",taskNotFind);
        Assertions.assertEquals(ERROR_RESULT.getMessage(),error);
    }

    @Test
    void executeCmd_GetUserTasksSort_ExpectedBehavior() {
        Mockito.when(simpleCache.getUser(1L)).thenReturn(user);
        String result = getExecutor.executeCmd(cmdGetUserTasksSort);
        Assertions.assertEquals(user.getTaskList().get(0).toString(),result);
    }

    @Test
    void executeCmd_GetUserTasksSort_NotFind_Or_WrongValue() {
        String result = getExecutor.executeCmd(cmdGetUserTasksSort);
        String wrongCommand="userTasksSort#123k";
        String error=getExecutor.executeCmd(wrongCommand);

        Assertions.assertEquals("User c id=1 не существует",result);
        Assertions.assertEquals(ERROR_RESULT.getMessage(),error);
    }

    @Test
    void executeCmd_GetUserTasksSort_EmptyTaskList() {
        User userEmpty=new User(2L,null);
        Mockito.when(simpleCache.getUser(1L)).thenReturn(userEmpty);
        String result = getExecutor.executeCmd(cmdGetUserTasksSort);
        Assertions.assertEquals(String.format("У User c id=%d нет назначенных задач", userEmpty.getId()),result);
    }
}