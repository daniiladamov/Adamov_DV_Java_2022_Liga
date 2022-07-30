package homework.service;

import homework.entity.task.Task;
import homework.entity.user.User;
import homework.repository.TaskRepo;
import homework.util.enums.EnumStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

class TaskServiceTest {
    @Mock
    private TaskRepo taskRepo;
    private TaskService taskService;
    private User user=new User();
    private Task task=new Task();

    public TaskServiceTest() {
        user.setId(1L);
        task.setId(1L);
        task.setUser(user);
        task.setDate(new Date());
        MockitoAnnotations.openMocks(this);
        user.setTaskList(new ArrayList<>());
        user.addTask(task);
        taskService=new TaskService(taskRepo);

    }

    @Test
    void removeTask_deleteRellyTask_ExpectedBehavior() {
        Mockito.doNothing().when(taskRepo).delete(task);
        Mockito.when(taskRepo.findById(1L)).thenReturn(Optional.ofNullable(task));
        taskService.removeTask(1L);
        Mockito.verify(taskRepo, Mockito.times(1)).delete(task);
    }
    @Test
    void removeTask_NullTask_ExpectedBehavior() {
        Throwable throwable= Assertions.assertThrows(NullPointerException.class,()->taskService.removeTask(null));
        Assertions.assertNotNull(throwable);
    }


    @Test
    void save_ExpectedBehavior() {
        Mockito.when(taskRepo.save(task)).thenReturn(task);
        taskService.save(task);
        Assertions.assertEquals(EnumStatus.EMPTY,task.getStatus());
        taskService.save(task);
        Mockito.verify(taskRepo,Mockito.times(2)).save(task);
    }

    @Test
    void save_NullEntity_ExpectedBehavior() {
        Throwable throwable= Assertions.assertThrows(NullPointerException.class,()->taskService.save(null));
        Assertions.assertNotNull(throwable);
    }

}