package homework.homework3.command;

import homework.homework2.service.FileService;
import homework.homework2.service.SimpleCache;

public class EndExecutor extends AbstractCommandExecutor {
    FileService fileService;

    public EndExecutor(String command, SimpleCache simpleCache, FileService fileService) {
        super(command, simpleCache);
        this.fileService = fileService;
    }

    @Override
    public String executeCmd() {
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
