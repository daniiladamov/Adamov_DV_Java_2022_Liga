package homework.mapper;

import homework.entity.EnumStatus;
import homework.entity.task.Task;
import homework.entity.user.User;
import homework.exception.MappingException;
import homework.service.TaskService;
import homework.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Optional;

class TaskMapperTest {
    @Mock
    private TaskService taskService;
    @Mock
    private UserService userService;
    private TaskMapper taskMapper;
    private String lineNewTask="new,ДЗ,Сделать прям много чего,1,26.09.2022";
    private String lineUpdTask="1,ДЗ,Сделать прям много чего,1,26.09.2022";
    private User user=new User();
    private Task task=new Task();

    public TaskMapperTest() {
        user.setId(1L);
        user.setName("Иван");
        task.setId(1L);
        task.setUser(user);
        task.setTitle("ДЗ");
        task.setDescription("Сделать прям много чего");
        task.setDate(GregorianCalendar.from(LocalDate.parse("26.09.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                .atStartOfDay(ZoneId.systemDefault())));
        MockitoAnnotations.openMocks(this);
        user.setTaskList(new ArrayList<>());
        user.addTask(task);
        taskMapper=new TaskMapper(taskService,userService);
    }

    @Test
    void mapToNewEntity_CreateNewTask_ExpectedBehavior() throws MappingException {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.ofNullable(user));
        String currentLine;
        for (EnumStatus enumStatus:EnumStatus.values()){
            if (enumStatus.equals(EnumStatus.EMPTY)){
                currentLine = lineNewTask;
                task.setStatus(EnumStatus.EMPTY);
            }
            else {
                currentLine = lineNewTask + "," + enumStatus.getStatus();
                task.setStatus(enumStatus);
            }
            Task createdTask = taskMapper.mapToNewEntity(currentLine);
            createdTask.setId(1L);
            Mockito.verify(taskService,Mockito.times(1)).save(createdTask);
            Assertions.assertEquals(task.toString(),createdTask.toString());
        }
    }

    @Test
    void mapToEntity_UpdTask_ExpectedBehavior() throws MappingException {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(taskService.getTask(1L)).thenReturn(Optional.ofNullable(task));
        String currentLine;
        for (EnumStatus enumStatus:EnumStatus.values()){
            if (enumStatus.equals(EnumStatus.EMPTY)){
                currentLine = lineUpdTask;
                task.setStatus(EnumStatus.EMPTY);
            }
            else {
                currentLine = lineUpdTask + "," + enumStatus.getStatus();
                task.setStatus(enumStatus);
            }
            Task createdTask = taskMapper.mapToEntity(currentLine);
            Assertions.assertEquals(task.toString(),createdTask.toString());
        }
    }

    @Test
    void mapToEntity_CreateTask_From_IncorrectLine(){
        String incorrectLine=lineNewTask+",124,123";
        Throwable throwable=Assertions.assertThrows(MappingException.class,()->taskMapper.mapToEntity(incorrectLine));
        Assertions.assertNotNull(throwable);
    }

}