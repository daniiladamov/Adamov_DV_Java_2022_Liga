package homework.homework3.service;

import homework.homework3.command.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommandService {
    private final GetExecutor getExecutor;
    private final PatchExecutor patchExecutor;
    private final PostExecutor postExecutor;
    private final DeleteExecutor deleteExecutor;
    private final PutExecutor putExecutor;
    private final EndExecutor endExecutor;
    private final CommandExecutor badExecutor = command -> "Неверно указан параметр запроса 'method'";

    public String getMessage(CommandEnum commandEnum, String command) {
        CommandExecutor commandExecutor = null;
        switch (commandEnum) {
            case GET:
                commandExecutor = getExecutor;
                break;
            case PATCH:
                commandExecutor = patchExecutor;
                break;
            case POST:
                commandExecutor = postExecutor;
                break;
            case DELETE:
                commandExecutor = deleteExecutor;
                break;
            case PUT:
                commandExecutor = putExecutor;
                break;
            case END:
                commandExecutor = endExecutor;
                break;
            case BAD:
                commandExecutor = badExecutor;
                break;
        }
        return commandExecutor.executeCmd(command);
    }
}
