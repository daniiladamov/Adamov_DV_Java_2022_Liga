package homework.homework3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class SimpleConfig {

    @Value("${files.users}")
    public String usersPath;
    @Value("${files.tasks}")
    public String tasksPath;

    @Bean(name = "users")
    public Path getUsersFile() {
        return Paths.get(usersPath);
    }

    @Bean(name = "tasks")
    public Path getTasksFile() {
        return Paths.get(tasksPath);
    }
}
