package homework.service;

import homework.entity.project.Project;
import homework.entity.task.Task;
import homework.entity.user.User;
import homework.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {
    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;
    @Value("${exception_message}")
    private String exceptionMessage;

    public Long createTask(Task task, Long userId, Long projectId) {
        Optional<User> userOptional = userService.getUser(userId);
        Optional<Project> projectOptional=projectService.getProject(projectId);
        if (userOptional.isPresent() && projectOptional.isPresent()) {
            User user=userOptional.get();
            Project project=projectOptional.get();
            task.setUser(user);
            task.setProject(project);
            project.addUser(user);
            return taskService.save(task);
        }
        else{
            String exception="";
            if(userOptional.isEmpty())
                exception=String.format(exceptionMessage+"\n",User.class.getSimpleName(), userId);
            if(projectOptional.isEmpty())
                exception+=String.format(exceptionMessage,Project.class.getSimpleName(), projectId);
            throw new EntityNotFoundException(exception);
        }
    }
}
