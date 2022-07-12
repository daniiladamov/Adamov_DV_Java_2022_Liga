package homework.command;

import org.springframework.stereotype.Component;

@Component
public class BadExecutor  implements CommandExecutor{
    @Override
    public String executeCmd(String command) {
        return "Неверно указан параметр запроса 'method'";
    }
}
