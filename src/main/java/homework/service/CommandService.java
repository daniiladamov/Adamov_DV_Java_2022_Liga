package homework.service;

import homework.command.*;
import homework.util.CommandEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandService {
    private final GetExecutor getExecutor;
    private final PatchExecutor patchExecutor;
    private final PostExecutor postExecutor;
    private final DeleteExecutor deleteExecutor;
    private final PutExecutor putExecutor;
    private final CommandExecutor badExecutor;

    public String getMessage(CommandEnum commandEnum, String command) {
        CommandExecutor commandExecutor = null;
        if (commandEnum == null)
            commandExecutor = badExecutor;
        else {
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
                case BAD:
                    commandExecutor = badExecutor;
                    break;
            }
        }
        return commandExecutor.executeCmd(command);
    }

    public String executeCommand(Map<String, String> commandMap) {
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
            return getMessage(commandEnum, cmd.get());
    }
}
