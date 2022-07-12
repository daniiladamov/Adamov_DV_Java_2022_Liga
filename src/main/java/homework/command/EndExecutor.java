package homework.command;

import homework.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static homework.util.MessageEnum.ERROR_RESULT;

@Component
@RequiredArgsConstructor
public class EndExecutor implements CommandExecutor {
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
        return ERROR_RESULT.getMessage();
    }
}