package homework.homework2;

import homework.homework2.service.FileService;
import homework.homework2.service.SimpleCache;
import homework.homework2.service.mapper.TaskMapper;
import homework.homework2.service.mapper.UserMapper;
import homework.homework2.shell.SimpleShellProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class StartApplication {

    private UserMapper userMapper;
    TaskMapper taskMapper;

    public StartApplication(UserMapper userMapper, TaskMapper taskMapper) {
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

    /**
     * Точка входа в программу
     *
     * @param args- если аргументы не передаются, работа идет с файлами по умолчаниями tasks.csv и users.csv,
     *              находящимися в директории src\main\resources.
     *              Если же аргументы передаются,то их должно быть два: первый - это абсолютный путь к файлу с
     *              пользователями,а второй - с задачами.
     */
    public static void main(String[] args) {
        SimpleCache simpleCache = new SimpleCache();
        UserMapper userMapper = new UserMapper(simpleCache);
        TaskMapper taskMapper = new TaskMapper(simpleCache);
        StartApplication sa = new StartApplication(userMapper, taskMapper);
        FileService service = sa.init(args, simpleCache);
        SimpleShellProvider shellProvider = new SimpleShellProvider(simpleCache, service, taskMapper);
        shellProvider.start();
    }

    private FileService init(String[] args, SimpleCache simpleCache) {

        if (args.length == 2) {
            boolean error = false;
            Path pathUser = Paths.get(args[0]);
            if (!pathUser.getFileName().toString().matches(".*\\.csv")) {
                System.err.println("Необходимо передачать файлы с расширением .csv");
                error = true;
            }
            Path pathTask = Paths.get(args[1]);
            if (!pathTask.getFileName().toString().matches(".*\\.csv")) {
                System.err.println("Необходимо передать файлы с расширением .csv");
                error = true;
            }
            if (error)
                throw new RuntimeException("Ошибка в расширении файла(-ов)");
            try {
                List<String> strings = Files.lines(pathUser).collect(Collectors.toList());
                userMapper.mapToEntityList(strings);

                List<String> stringsTasks = Files.lines(pathTask).collect(Collectors.toList());
                taskMapper.mapToEntityList(stringsTasks);

                FileService fileService = new FileService(simpleCache, taskMapper, userMapper);
                return fileService;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (args.length == 0) {
            try {
                Path pathUser = Paths.get("src/main/resources/users.csv");
                List<String> strings = Files.lines(pathUser).collect(Collectors.toList());
                userMapper.mapToEntityList(strings);

                Path pathTask = Paths.get("src/main/resources/tasks.csv");
                List<String> stringsTasks = Files.lines(pathTask).collect(Collectors.toList());
                taskMapper.mapToEntityList(stringsTasks);

                FileService fileService = new FileService(simpleCache, taskMapper, userMapper);
                return fileService;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Количество аргументов должно быть равно двум или нулю");
            System.exit(1);
        }
        return null;
    }

}
