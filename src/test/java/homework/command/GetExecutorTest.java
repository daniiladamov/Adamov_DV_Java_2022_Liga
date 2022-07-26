package homework.command;

import homework.entity.task.Task;
import homework.entity.user.User;
import homework.service.TaskService;
import homework.service.UserService;
import homework.util.EnumStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import static homework.util.MessageEnum.ERROR_RESULT;

class GetExecutorTest {
    @Mock
    private TaskService taskService;
    @Mock
    private UserService userService;
    private String cmdGetUserTasks="userTasks#1";
    private String cmdGetUserTasksSort="userTasksSort#1";
    private String cmdTaskById="task#1";
    private GetExecutor getExecutor;
    private User user=new User();
    private Task task=new Task();

    public GetExecutorTest() {
        user.setId(1L);
        task.setId(1L);
        task.setUser(user);
        task.setDate(Calendar.getInstance());
        task.setStatus(EnumStatus.EMPTY);
        MockitoAnnotations.openMocks(this);
        user.addTask(task);
        getExecutor=new GetExecutor(taskService,userService);
    }

    @Test
    void executeCmd_GetUserTasks_ExpectedBehavior() {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.ofNullable(user));
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
        User userEmpty=new User();
        userEmpty.setId(2L);
        userEmpty.setTaskList(new ArrayList<>());
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.of(userEmpty));
        String result = getExecutor.executeCmd(cmdGetUserTasks);
        Assertions.assertEquals(String.format("У User c id=%d нет назначенных задач", userEmpty.getId()),result);
    }

    @Test
    void executeCmd_GetTaskById_ExpectedBehavior() {
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(task));
        String s = getExecutor.executeCmd(cmdTaskById);
        Assertions.assertEquals(task.toString(),s);
    }

    @Test
    void executeCmd_GetTaskById_NotFind_Or_WrongValue() {
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(null));
        String taskNotFind = getExecutor.executeCmd(cmdTaskById);
        String wrongCommand="task#123k";
        String error=getExecutor.executeCmd(wrongCommand);

        Assertions.assertEquals("Task c id=1 не существует",taskNotFind);
        Assertions.assertEquals(ERROR_RESULT.getMessage(),error);
    }

    @Test
    void executeCmd_GetUserTasksSort_ExpectedBehavior() {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.ofNullable(user));
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
        User userEmpty=new User();
        userEmpty.setId(2L);
        userEmpty.setTaskList(new ArrayList<>());
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.of(userEmpty));
        String result = getExecutor.executeCmd(cmdGetUserTasksSort);
        Assertions.assertEquals(String.format("У User c id=%d нет назначенных задач", userEmpty.getId()),result);
    }
}