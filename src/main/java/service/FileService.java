package service;

import service.mapper.TaskMapper;
import service.mapper.UserMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileService {
    private Path usersFile;
    private Path tasksFile;
    private SimpleCache simpleCache;
    private TaskMapper taskMapper;
    private UserMapper userMapper;

    public FileService(Path usersFile, Path tasksFile, SimpleCache simpleCache, TaskMapper taskMapper,
                       UserMapper userMapper) {
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
}
