package shell;

import entity.EnumStatus;
import entity.task.Task;
import entity.task.TaskComparator;
import entity.user.User;
import exception.MappingException;
import service.FileService;
import service.SimpleCache;
import service.mapper.TaskMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleShellProvider {

    private SimpleCache simpleCache;
    private FileService fileService;
    private TaskMapper taskMapper;

    public SimpleShellProvider(SimpleCache simpleCache, FileService fileService, TaskMapper taskMapper) {
        this.simpleCache = simpleCache;
        this.fileService = fileService;
        this.taskMapper = taskMapper;
    }

    private static Pattern userTask = Pattern.compile("get\\s+userTask/\\d+", Pattern.CASE_INSENSITIVE);
    private static Pattern userSortTask = Pattern.compile("get\\s+userTaskSort/\\d+", Pattern.CASE_INSENSITIVE);
    private static Pattern changeTaskStatus = Pattern.compile("patch\\s+task/\\d+\\s+status=.+",
            Pattern.CASE_INSENSITIVE);
    private static Pattern addTask = Pattern.compile("post\\s+task\\s+.*", Pattern.CASE_INSENSITIVE);
    private static Pattern getTaskById = Pattern.compile("get\\s+task/\\d+");
    private static Pattern deleteTaskById = Pattern.compile("delete\\s+task/\\d+");
    private static Pattern putTask = Pattern.compile("put\\s+task\\s+.*", Pattern.CASE_INSENSITIVE);


    private static String startInfo = "-------------------------\n" +
            "Для получение информации о командах введите в коммандной строке:\ninfo\n" +
            "-------------------------\n";
    private static String cmdInfo = "-------------------------\n" +
            "Допустпыне коммнады (регистр и количество пробелов не имеют значения):\n" +
            "get userTask/X - отображается список задач пользователя с id=Х;\n" +
            "get userTaskSort/X - отображается отсортированный список задач пользователя с id=Х;\n" +
            "patch task/X status=Y - изменяет стату задачи с id=X на status=Y" +
            "post task T,D,ID,DATE,S - добавляет задачу, где T - заголовок; D - описание заадчи; ID - идентификатор " +
            "пользователя; DATE - дата окончания задачи (в формате ДД.ММ.ГГГГ); S - статус задачи (передается " +
            "опционально);\n" +
            "get task/X - получение задачи с id=X;\n" +
            "delete task/X - удаление задачи с id=X;\n" +
            "put task TID,T,D,ID,DATE,S обновляет данные для задачи с id=TID, остальные поля в соответсвии " +
            "с командой 'post' (см. выше);\n" +
            "clear all - очисть состояние программы и удалить все данные из соответсвующих файлов;\n" +
            "save - сохранить состояние программы;\n" +
            "exit save - завершение работы с коммандной строкой и сохранение состояния;\n" +
            "exit - завершение работы с коммандной строкой без сохранения состояния.\n" +
            "-------------------------\n";

    public void start() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(startInfo);
        while (true) {
            System.out.println("Введите команду:");
            try {
                String cmd = bufferedReader.readLine();
                if (cmd.matches("(?i)exit(\\s+)?"))
                    return;
                if (cmd.matches("(?i)exit\\s+save(\\s+)?")) {
                    fileService.save();
                    return;
                }
                if (cmd.matches("(?i)clear\\s+all(\\s+)?")) {
                    fileService.clearAll();
                    continue;
                }
                if (cmd.matches("(?i)save(\\s+)?")) {
                    fileService.save();
                    continue;
                }
                parseAndExecute(cmd);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    bufferedReader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                throw new RuntimeException(e);
            }
        }
    }

    private void parseAndExecute(String cmd) {
        if (cmd.equalsIgnoreCase("info")) {
            System.out.println(cmdInfo);
            return;
        }
        if (userTask.matcher(cmd).matches()) {
            getUserTask(cmd);
            return;
        }

        if (userSortTask.matcher(cmd).matches()) {
            getUserSortTask(cmd);
            return;
        }

        if (changeTaskStatus.matcher(cmd).matches()) {
            patchTaskStatus(cmd);
            return;
        }

        if (addTask.matcher(cmd).matches()) {
            postTask(cmd);
            return;
        }

        if (getTaskById.matcher(cmd).matches()) {
            getTaskById(cmd);
            return;
        }

        if (deleteTaskById.matcher(cmd).matches()) {
            deleteTaskById(cmd);
            return;
        }

        if (putTask.matcher(cmd).matches()) {
            putTask(cmd);
            return;
        }
        System.err.println("Нет такой команды или команда введена неверно");
    }

    private void putTask(String cmd) {
        String parse = cmd.replaceAll("(?i)put\\s+task\\s+", "");
        taskMapper.setUpdate(true);
        try {
            Task task = taskMapper.mapToEntity(parse);
            System.out.println("Задача обновлена:\n" + task);
        } catch (MappingException e) {
            System.err.println(e.getMessage());
        }

    }

    private void deleteTaskById(String cmd) {
        Long id = Long.parseLong(cmd.replaceAll("(?i)delete\\s+task/", ""));
        Task task = simpleCache.getTask(id);
        if (task == null)
            System.err.println(String.format("Task c id=%d не существует", id));
        else {
            simpleCache.removeTask(id);
            System.out.println("Задача была удалена.\n" + task);
        }
    }

    private void getTaskById(String cmd) {
        Long id = Long.parseLong(cmd.replaceAll("(?i)get\\s+task/", ""));
        Task task = simpleCache.getTask(id);
        if (task == null)
            System.err.println(String.format("Task c id=%d не существует", id));
        else
            System.out.println(task);
    }

    private void getUserTask(String cmd) {
        Long id = Long.parseLong(cmd.replaceAll("(?i)get\\s+userTask/", ""));
        User userFind = simpleCache.getUser(id);
        if (userFind == null)
            System.err.println(String.format("User c id=%d не существует", id));
        else if (userFind.getTaskList().size() == 0)
            System.err.println(String.format("У User c id=%d нет назначенных задач", id));
        else
            userFind.getTaskList().forEach(x -> System.out.println(x.toString()));
    }

    private void getUserSortTask(String cmd) {
        Long id = Long.parseLong(cmd.replaceAll("(?i)get\\s+userTaskSort/", ""));
        User userFind = simpleCache.getUser(id);
        if (userFind == null)
            System.err.println(String.format("User c id=%d не существует", id));
        else if (userFind.getTaskList().size() == 0)
            System.err.println(String.format("У User c id=%d нет назначенных задач", id));
        else
            userFind.getTaskList().stream().sorted(new TaskComparator()).forEach(System.out::println);
    }

    private void patchTaskStatus(String cmd) {
        String parse = cmd.replaceAll("(?i)patch\\s+task/", "")
                .replaceAll("\\s+status=", ";");
        String[] split = parse.split(";");
        Long id = Long.parseLong(split[0]);
        Task taskFind = simpleCache.getTask(id);
        if (taskFind == null)
            System.err.println(String.format("Task c id=%d не существует", id));
        else {
            EnumStatus status = Arrays.stream(EnumStatus.values()).filter(sts -> sts.getStatus()
                    .equalsIgnoreCase(split[1]))
                    .findFirst().orElse(null);
            if (status == null)
                System.err.println("Статус может принимать значения:" + Arrays.stream(EnumStatus.values()).map(sts ->
                        sts.getStatus()).collect(Collectors.joining(",")));
            else {
                taskFind.setStatus(status);
                System.out.println("Статус обновлен");
            }
        }
    }

    private void postTask(String cmd) {
        String lineToParse = cmd.replaceAll("(?i)post\\s+task\\s+", "").trim();
        try {
            Task task = taskMapper.mapToNewEntity(lineToParse);
            System.out.println("Создана Task c id=" + task.getId());
        } catch (MappingException e) {
            System.err.println(e.getMessage());
        }

    }

}
