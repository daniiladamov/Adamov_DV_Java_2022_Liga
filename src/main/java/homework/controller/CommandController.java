package homework.controller;

import homework.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommandController {
    private final CommandService commandService;

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
        return commandService.executeCommand(commandMap);
    }
}
