package homework.service;

import com.sun.istack.NotNull;
import homework.entity.Project;
import homework.entity.Task;
import homework.entity.User;
import homework.exception.EntityNotFoundException;
import homework.repository.TaskRepo;
import homework.util.CustomPage;
import homework.util.Specifications;
import homework.util.enums.EnumStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final TaskRepo taskRepo;
    @PersistenceContext
    private EntityManager entityManager;
    @Value("${exception_message}")
    private String exceptionMessage;

    @PostAuthorize("hasRole('ADMIN') || " +
            "(returnObject.isPresent() && returnObject.get().user.login==authentication.name)")
    public Task getTask(@NonNull Long id) {
        return taskRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(exceptionMessage, Task.class.getSimpleName(), id)));
    }

    @Transactional
    public boolean removeTask(@NonNull Long id) {
        removeTask(getTask(id));
        return true;
    }

    @Transactional
    public Task updateTask(@NonNull Task task) {
        return taskRepo.save(task);
    }

    @Transactional
    public Long save(@NotNull Task task) {
        if (task.getStatus() == null)
            task.setStatus(EnumStatus.EMPTY);
        taskRepo.save(task);
        return task.getId();
    }

    public Page<Task> getTasks(CustomPage taskPage) {
        Sort sort = Sort.by(taskPage.getSortDirection(), taskPage.getSortBy());
        Pageable pageable = PageRequest.of(taskPage.getPageNumber(), taskPage.getPageSize(), sort);
        return taskRepo.findAll(pageable);
    }

    private Page<Task> getTasksByUser(User user, Pageable pageable) {
        return taskRepo.findAll(Specifications.getUserTasks(user), pageable);
    }

    private Page<Task> getTasksByProject(Project project, Pageable pageable) {
        return taskRepo.findAll(Specifications.getProjectTasks(project), pageable);
    }

    @Transactional
    public Long createTask(Task task, User user, Project project) {
        task.setUser(user);
        task.setProject(project);
        project.addUser(user);
        return save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task task) {
        Task taskInBd = getTask(id);
        task.setId(id);
        task.setComment(taskInBd.getComment());
        task.setUser(taskInBd.getUser());
        task.setProject(taskInBd.getProject());
        return updateTask(task);
    }
    @Transactional
    public void removeTask(Task task) {
            long taskForUserByProject=task.getUser().getTaskList().stream().
                    filter(t->t.getProject().equals(task.getProject())).count();
            if (taskForUserByProject==1){
                entityManager.createNativeQuery("delete from project_user where project_id= :id and " +
                                "user_id= :userId")
                        .setParameter("id",task.getProject().getId())
                        .setParameter("userId",task.getUser().getId()).executeUpdate();
            }
            taskRepo.delete(task);
    }
    @PostAuthorize("hasRole('ADMIN') || returnObject.getContent().get(0).user.login==authentication.name")
    public Page<Task> getUserTasks(User user,Pageable pageable) {
        return getTasksByUser(user, pageable);
    }

    public Page<Task> getProjectTasks(Project project, Pageable pageable) {
        return getTasksByProject(project, pageable);

    }
}
