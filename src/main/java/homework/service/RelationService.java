package homework.service;

import homework.entity.comment.Comment;
import homework.entity.project.Project;
import homework.entity.task.Task;
import homework.entity.user.User;
import homework.exception.EntityNotFoundException;
import homework.util.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RelationService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CommentService commentService;
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

    public Long createComment(Comment comment, Long taskId) {
        Optional<Task> taskOptional=taskService.getTask(taskId);
        if (taskOptional.isPresent()){
            comment.setTask(taskOptional.get());
            return commentService.create(comment).getId();
        }
        else
            throw new EntityNotFoundException(String.format(exceptionMessage,Task.class.getSimpleName(),taskId));
    }

    public void deleteUser(Long userId) {
        Optional<User> userOptional=userService.getUser(userId);
        if (userOptional.isPresent()){
            entityManager.createNativeQuery("delete from project_user where user_id= :id")
                    .setParameter("id",userId).executeUpdate();
            userService.deleteUser(userId);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,User.class.getSimpleName(),userId));
    }

    public void deleteProject(Long projectId) {
        Optional<Project> projectOptional=projectService.getProject(projectId);
        if (projectOptional.isPresent()){
            entityManager.createNativeQuery("delete from project_user where project_id= :id")
                    .setParameter("id",projectId).executeUpdate();
            projectService.deleteProject(projectId);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(),projectId));
    }

    public Task updateTask(Task task) {
        Optional<Task> taskOptional=taskService.getTask(task.getId());
        if (taskOptional.isPresent()){
            Task taskInBd=taskOptional.get();
            task.setComment(taskInBd.getComment());
            task.setUser(taskInBd.getUser());
            task.setProject(taskInBd.getProject());
            return taskService.updateTask(task);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Task.class.getSimpleName(),task.getId()));
    }

    public Comment updateComment(Comment comment) {
        Optional<Comment> commentOptional=commentService.getComment(comment.getId());
        if (commentOptional.isPresent()){
            Comment commentInBd=commentOptional.get();
            comment.setTask(commentInBd.getTask());
            return commentService.create(comment);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Comment.class.getSimpleName(),comment.getId()));
    }

    public void removeTask(Long id) {
        Optional<Task> taskOptional=taskService.getTask(id);
        if (taskOptional.isPresent()){
            Task task=taskOptional.get();
            long taskForUserByProject=task.getUser().getTaskList().stream().
                    filter(t->t.getProject().equals(task.getProject())).count();
            if (taskForUserByProject==1){
                entityManager.createNativeQuery("delete from project_user where project_id= :id and " +
                                "user_id= :userId")
                        .setParameter("id",task.getProject().getId())
                        .setParameter("userId",task.getUser().getId()).executeUpdate();
            }
            taskService.removeTask(id);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Task.class.getSimpleName(),id));
    }
    public Page<Project> getUserProjects(Long id, CustomPage customPage) {
        Optional<User> userOptional=userService.getUser(id);
        if (userOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return projectService.getProjectsByUser(userOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                String.format(exceptionMessage,User.class.getSimpleName(), id));
    }

    public Page<Task> getUserTasks(Long id, CustomPage customPage) {
        Optional<User> userOptional=userService.getUser(id);
        if (userOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return taskService.getTasksByUser(userOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,User.class.getSimpleName(), id));
    }

    public Page<Task> getProjectTasks(Long id, CustomPage customPage) {
        Optional<Project> projectOptional=projectService.getProject(id);
        if (projectOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return taskService.getTasksByProject(projectOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(), id));
    }

    public Page<User> getProjectUsers(Long id, CustomPage customPage) {
        Optional<Project> projectOptional=projectService.getProject(id);
        if (projectOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return userService.getUsersByProject(projectOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(), id));
    }

    public Page<Comment> getTaskComments(Long id, CustomPage customPage) {
        Optional<Task> taskOptional=taskService.getTask(id);
        if (taskOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return commentService.getComemntsByTask(taskOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Task.class.getSimpleName(), id));
    }
}
