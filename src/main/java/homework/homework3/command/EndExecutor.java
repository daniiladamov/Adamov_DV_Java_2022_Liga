package homework.homework3.command;

import homework.homework2.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EndExecutor extends AbstractCommandExecutor {
    private final FileService fileService;

    @Override
    public String executeCmd(String command) {
        if (command.matches("save")) {
            fileService.save();
            return "Изменения сохранены в соответсвующих файлах";
        }
        if (command.matches("clear")) {
            fileService.clearAll();
            return "Все строки удалены из соответсвующих файлов";
        }
        return errorResult;
    }
}
