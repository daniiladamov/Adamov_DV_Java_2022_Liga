package homework.homework3.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@AllArgsConstructor
public class SimpleConfig {

    @Bean(name="users")
    public Path getUsersFile(){
        return Paths.get("src\\main\\resources\\users.csv");
    }
    @Bean(name="tasks")
    public Path getTasksFile(){
        return Paths.get("src\\main\\resources\\tasks.csv");
    }
}
