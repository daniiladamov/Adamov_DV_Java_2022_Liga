package homework.homework3;

import homework.homework2.shell.SimpleShellProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("homework")
public class StartApplication3 {
    /**
     * Точка входа для выполнения програмы по homework-3
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(StartApplication3.class, args);
        SimpleShellProvider shellProvider=run.getBean("simpleShellProvider",SimpleShellProvider.class);
        shellProvider.start();
    }
}
