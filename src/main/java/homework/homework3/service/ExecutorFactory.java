package homework.homework3.service;

import homework.homework2.service.FileService;
import homework.homework2.service.SimpleCache;
import homework.homework2.service.mapper.TaskMapper;
import homework.homework3.command.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExecutorFactory {
    SimpleCache simpleCache;
    TaskMapper taskMapper;
    FileService fileService;

    public CommandExecutor getExecutor(CommandEnum commandEnum, String command) {
        switch (commandEnum) {
            case GET:
                return new GetExecutor(command, simpleCache);
            case PATCH:
                return new PatchExecutor(command, simpleCache);
            case POST:
                return new PostExecutor(command, simpleCache, taskMapper);
            case DELETE:
                return new DeleteExecutor(command, simpleCache);
            case PUT:
                return new PutExecutor(command, simpleCache, taskMapper);
            case END:
                return new EndExecutor(command, simpleCache, fileService);
            case BAD:
                return () -> "Неверно указан параметр запроса 'method'";
        }
        return null;
    }
}
