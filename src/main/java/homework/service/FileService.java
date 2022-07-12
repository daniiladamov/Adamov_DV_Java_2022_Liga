package homework.service;

import homework.mapper.TaskMapper;
import homework.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Value("${files.users}")
    private String usersPath;
    @Value("${files.tasks}")
    private String tasksPath;
    private final SimpleCache simpleCache;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    public FileService(SimpleCache simpleCache, TaskMapper taskMapper, UserMapper userMapper) {
        this.simpleCache = simpleCache;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
    }

    public void clearAll() {
        boolean flag = true;
        if (!Files.isWritable(Paths.get(usersPath))) {
            System.err.println("Файл " + Paths.get(usersPath).getFileName() + " не досутпен для записи");
            flag = false;
        }
        if (!Files.isWritable(Paths.get(tasksPath))) {
            System.err.println("Файл " + Paths.get(tasksPath).getFileName() + " не досутпен для записи");
            flag = false;
        }
        if (!flag)
            return;
        try {
            Files.writeString(Paths.get(usersPath), "");
            Files.writeString(Paths.get(tasksPath), "");
            simpleCache.removeCache();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void save() {
        List<String> tasks = taskMapper.mapToStringList(simpleCache.getTasks());
        List<String> users = userMapper.mapToStringList(simpleCache.getUsers());
        try {
            Files.write(Paths.get(usersPath), users);
            Files.write(Paths.get(tasksPath), tasks);
            System.out.println("состояние сохранено");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @PostConstruct
    private void mappingFiles() {
        try {
            List<String> strings = Files.lines(Paths.get(usersPath)).collect(Collectors.toList());
            userMapper.mapToEntityList(strings);

            List<String> stringsTasks = Files.lines(Paths.get(tasksPath)).collect(Collectors.toList());
            taskMapper.mapToEntityList(stringsTasks);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
