package homework.service;

import com.sun.istack.NotNull;
import homework.entity.project.Project;
import homework.entity.task.Task;
import homework.entity.task.TaskFilter;
import homework.entity.task.Task_;
import homework.entity.user.User;
import homework.entity.user.User_;
import homework.exception.EntityNotFoundException;
import homework.repository.TaskRepo;
import homework.util.CustomPage;
import homework.util.enums.EnumStatus;
import homework.util.Specifications;
import lombok.NonNull;
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
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public Optional<Task> getTask(@NonNull Long id) {
        return taskRepo.findById(id);
    }

    @Transactional
    public boolean removeTask(@NonNull Long id) {
        Optional<Task> task = getTask(id);
        if (task.isPresent()) {
            taskRepo.delete(task.get());
            return true;
        } else
            return false;
    }

    @Transactional
    public Task updateTask(@NonNull Task task){
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

    public List<Task> getTaskMaxCount(TaskFilter taskFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = criteriaBuilder.createQuery(Task.class);
        Root<Task> root = cq.from(Task.class);
        Predicate[] predicates = getPredicateList(taskFilter, criteriaBuilder, root).toArray(Predicate[]::new);
        cq.where(predicates);
        return entityManager.createQuery(cq).getResultList();
    }

    private User getMaxTasksUser() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<Task> root = cq.from(Task.class);
        Join<Task, User> user = root.join(Task_.user);
        cq.select(user)
                .groupBy(user.get(User_.id))
                .orderBy(cb.desc(cb.count(root.get(Task_.id))));
        return entityManager.createQuery(cq).getResultList().get(0);
    }

    private List<Predicate> getPredicateList(TaskFilter taskFilter, CriteriaBuilder criteriaBuilder, Root<Task> root) {
        List<Predicate> list = new ArrayList<>();
        User maxUser = getMaxTasksUser();
        if (maxUser != null) {
            Predicate equal = criteriaBuilder.equal(root.get(Task_.user), maxUser);
            list.add(equal);
        }
        if (taskFilter == null)
            return list;
        if (taskFilter.getEnumStatuses() != null) {
            Predicate status = root.get("status").in(taskFilter.getEnumStatuses());
            list.add(status);
        }
        if (taskFilter.getDateFrom() != null) {
            Predicate status = criteriaBuilder.greaterThanOrEqualTo(root.get("date"),
                    getDateFormat(taskFilter.getDateFrom()));
            list.add(status);
        }
        if (taskFilter.getDateTo() != null) {
            Predicate status = criteriaBuilder.lessThanOrEqualTo(root.get("date"),
                    getDateFormat(taskFilter.getDateTo()));
            list.add(status);
        }
        return list;
    }

    private static Calendar getDateFormat(String stringDate) {
        LocalDate date = LocalDate.parse(stringDate, dateFormat);
        return GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
    }

    private Page<Task> getTasksByUser(User user, Pageable pageable) {
        return taskRepo.findAll(Specifications.getUserTasks(user),pageable);
    }

    private Page<Task> getTasksByProject(Project project, Pageable pageable) {
        return taskRepo.findAll(Specifications.getProjectTasks(project),pageable);
    }
    @Transactional
    public Long createTask(Task task, Optional<User> userOptional, Optional<Project> projectOptional,
                           Long userId, Long projectId) {
        if (userOptional.isPresent() && projectOptional.isPresent()) {
            User user=userOptional.get();
            Project project=projectOptional.get();
            task.setUser(user);
            task.setProject(project);
            project.addUser(user);
            return save(task);
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
    @Transactional
    public Task updateTask(Optional<Task> taskOptional, Task task) {
        if (taskOptional.isPresent()){
            Task taskInBd=taskOptional.get();
            task.setComment(taskInBd.getComment());
            task.setUser(taskInBd.getUser());
            task.setProject(taskInBd.getProject());
            return updateTask(task);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Task.class.getSimpleName(),task.getId()));
    }
    @Transactional
    public void removeTask(Optional<Task> taskOptional, Long id) {
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
            removeTask(id);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Task.class.getSimpleName(),id));
    }

    public Page<Task> getUserTasks(Optional<User> userOptional, Long id, CustomPage customPage) {
        if (userOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return getTasksByUser(userOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,User.class.getSimpleName(), id));
    }

    public Page<Task> getProjectTasks(Optional<Project> projectOptional, Long id, CustomPage customPage) {
        if (projectOptional.isPresent()){
            Sort sort = Sort.by(customPage.getSortDirection(), customPage.getSortBy());
            Pageable pageable = PageRequest.of(customPage.getPageNumber(), customPage.getPageSize(), sort);
            return getTasksByProject(projectOptional.get(),pageable);
        }
        else
            throw new EntityNotFoundException(
                    String.format(exceptionMessage,Project.class.getSimpleName(), id));
    }
}
