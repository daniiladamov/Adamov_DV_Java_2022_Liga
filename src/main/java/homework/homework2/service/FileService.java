package homework.homework2.service;

import homework.homework2.service.mapper.TaskMapper;
import homework.homework2.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private Path usersFile;

    private Path tasksFile;
    private SimpleCache simpleCache;
    private TaskMapper taskMapper;
    private UserMapper userMapper;

    public FileService(@Qualifier("users")Path usersFile, @Qualifier("tasks")Path tasksFile, SimpleCache simpleCache,
                       TaskMapper taskMapper, UserMapper userMapper) {
        this.usersFile = usersFile;
        this.tasksFile = tasksFile;
        this.simpleCache = simpleCache;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
    }

    public void clearAll() {
        boolean flag = true;
        if (!Files.isWritable(usersFile)) {
            System.err.println("Файл " + usersFile.getFileName() + " не досутпен для записи");
            flag = false;
        }
        if (!Files.isWritable(tasksFile)) {
            System.err.println("Файл " + tasksFile.getFileName() + " не досутпен для записи");
            flag = false;
        }
        if (!flag)
            return;
        try {
            Files.writeString(usersFile, "");
            Files.writeString(tasksFile, "");
            simpleCache.removeCache();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void save() {
        List<String> tasks = taskMapper.mapToStringList(simpleCache.getTasks());
        List<String> users = userMapper.mapToStringList(simpleCache.getUsers());
        try {
            Files.write(usersFile, users);
            Files.write(tasksFile, tasks);
            System.out.println("состояние сохранено");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    @PostConstruct
    public void mappingFiles(){
        try {
            List<String> strings = Files.lines(usersFile).collect(Collectors.toList());
            userMapper.mapToEntityList(strings);

            List<String> stringsTasks = Files.lines(tasksFile).collect(Collectors.toList());
            taskMapper.mapToEntityList(stringsTasks);
        }
        catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
