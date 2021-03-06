package homework.controller;

import homework.entity.task.TaskDto;
import homework.entity.task.TaskFilter;
import homework.service.CommandService;
import homework.service.TaskService;
import homework.util.CommandEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommandController {
    private final CommandService commandService;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    /**
     * Допустпыне коммнады, передающиеся как параметры запроса по адресу http://localhost:8080/command (пробелы в параметре
     * 'cmd' заменяются на символ '_'):
     * method=get&cmd=userTasks#X - отображается список задач пользователя с id=Х;
     * method=get&cmd=userTasksSort#X - отображается отсортированный список задач по статусу для пользователя с id=Х;
     * method=get&cmd=task#X - получение задачи с id=X;
     * method=patch&cmd=task#X;status#Y - изменяет стату задачи с id=X на status=Y;
     * method=post&cmd=T,D,ID,DATE,S - добавляет задачу, где T - заголовок; D - описание заадчи; ID - идентификатор
     * пользователя; DATE - дата окончания задачи (в формате ДД.ММ.ГГГГ); S - статус задачи (передается опционально);
     * method=delete&cmd=task#X - удаление задачи с id=X;
     * method=put&cmd=TID,T,D,ID,DATE,S обновляет данные для задачи с id=TID, остальные поля в соответсвии с
     * командой 'post' (см. выше);
     * method=end&cmd=clear - удалить все данные из соответсвующих файлов;
     * method=end&cmd=save - сохранить все изменения.
     */
    @GetMapping("/command")
    public String responseForCommand(@RequestParam Map<String, String> commandMap) {
        String method = commandMap.get("method");
        CommandEnum commandEnum;
        try {
            commandEnum = CommandEnum.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException ex) {
            commandEnum = CommandEnum.BAD;
        }
        Optional<String> cmd = Optional.ofNullable(commandMap.get("cmd"));
        if (cmd.isEmpty())
            return "В запросе необходимо передать параметр 'cmd'";
        else
            return commandService.getMessage(commandEnum, cmd.get());
    }

    /**
     * Эндпоинт для homework-9. Тестировать можно по адресу: http://localhost:8080/swagger-ui/#
     * @param taskFilter объект передается в качестве параметров запроса. Используется для фильтрации
     *                   полученного списка задач по статусу и интервалу дат.
     *                   ВАЖНО: дата передается в формате dd-MM-yyyy.
     * @return  список задачи пользователя с наибольшим количеством задач. Опционально моежт быть отфильтрован.
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTaskMaxCount(TaskFilter taskFilter){
        return new ResponseEntity<>(taskService.getTaskMaxCount(taskFilter).stream().
                map(task->modelMapper.map(task, TaskDto.class)).collect(Collectors.toList()), HttpStatus.OK);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity getException(){
        return new ResponseEntity("Необходимо передавать дату в формате dd-MM-yyyy",
                HttpStatus.I_AM_A_TEAPOT);
    }

//    @GetMapping("/tasks")
//    public ResponseEntity<Page<TaskDto>>getTasks(TaskPage taskPage){
//        return new ResponseEntity<>(getOtjectToDto(taskService.getPages(taskPage)), HttpStatus.OK);
//    }
//
//    private Page<TaskDto> getOtjectToDto(Page<Task> tasksPage){
//        List<TaskDto> collect = tasksPage.getContent().stream().map(task -> modelMapper.map(task, TaskDto.class))
//                .collect(Collectors.toList());
//        return new PageImpl<>(collect,tasksPage.getPageable(), tasksPage.getTotalElements());
//    }

}
