package homework.homework2.service.mapper;

import homework.homework2.entity.EnumStatus;
import homework.homework2.entity.task.Task;
import homework.homework2.entity.user.User;
import homework.homework2.exception.MappingException;
import homework.homework2.service.SimpleCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class TaskMapperTest {
    @Mock
    private SimpleCache simpleCache;
    private TaskMapper taskMapper;
    private String line="1,ДЗ,Сделать прям много чего,1,26.09.2022";
    private User user=new User(1L,"Иван");
    private Task task=new Task(1L,"ДЗ","Сделать прям много чего",user,
            LocalDate.parse("26.09.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy")));

    public TaskMapperTest() {
        MockitoAnnotations.openMocks(this);
        taskMapper=new TaskMapper(simpleCache);
    }

    @Test
    void mapToEntity_CreateTask_ExpectedBehavior() throws MappingException {
        Mockito.when(simpleCache.getUser(1L)).thenReturn(user);
        String currentLine;
        for (EnumStatus enumStatus:EnumStatus.values()){
            if (enumStatus.equals(EnumStatus.EMPTY)){
                currentLine = line;
                task.setStatus(EnumStatus.EMPTY);
            }
            else {
                currentLine = line + "," + enumStatus.getStatus();
                task.setStatus(enumStatus);
            }
            Task createdTask = taskMapper.mapToEntity(currentLine);
            Mockito.verify(simpleCache,Mockito.times(1)).addTask(createdTask);
            Assertions.assertEquals(task.toString(),createdTask.toString());
        }
    }

    @Test
    void mapToEntity_CreateTask_From_IncorrectLine(){
        String incorrectLine=line+",124,123";
        Throwable throwable=Assertions.assertThrows(MappingException.class,()->taskMapper.mapToEntity(incorrectLine));
        Assertions.assertNotNull(throwable);
    }

}