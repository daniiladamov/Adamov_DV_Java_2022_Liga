package homework.service;

import com.sun.istack.NotNull;
import homework.entity.EnumStatus;
import homework.entity.task.Task;
import homework.entity.task.TaskFilter;
import homework.entity.task.Task_;
import homework.entity.user.User;
import homework.entity.user.User_;
import homework.repository.TaskRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    public void save(@NotNull Task task) {
        if (task.getStatus() == null)
            task.setStatus(EnumStatus.EMPTY);
        taskRepo.save(task);
    }

    public List<Task> getTaskMaxCount(TaskFilter taskFilter) {
        return taskRepo.findAll(tasksByUser(taskFilter,getMaxTasksUser()));
    }
    private User getMaxTasksUser() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<Task> root = cq.from(Task.class);
        Join<Task, User> user = root.join(Task_.user);
        cq
                .select(root.get(Task_.user))
                .groupBy(user.get(User_.id))
                .orderBy(cb.desc(cb.count(root.get(Task_.user))));
        return entityManager.createQuery(cq).getResultList().get(0);
    }
    private static Specification<Task> tasksByUser(TaskFilter taskFilter, User user){
        Specification<Task> specification;
        if (Objects.nonNull(user)) {
            specification= (root, query, criteriaBuilder1) ->
                    criteriaBuilder1.equal(root.get(Task_.user), user);
        }
        else
            return null;
        if (Objects.isNull(taskFilter))
            return specification;
        if (Objects.nonNull(taskFilter.getEnumStatuses())) {
            specification=specification.and((Specification<Task>) (root, query, criteriaBuilder12) ->
                    root.get("status").in(taskFilter.getEnumStatuses()));
        }
        if (Objects.nonNull(taskFilter.getDateFrom())) {
            specification=specification.and((Specification<Task>) (root, query, criteriaBuilder13) ->
                    criteriaBuilder13.greaterThanOrEqualTo(
                            root.get("date"), getDateFormat(taskFilter.getDateFrom())
                    ));
        }
        if (Objects.nonNull(taskFilter.getDateTo())) {
            specification=specification.and((Specification<Task>) (root, query, criteriaBuilder14) ->
                    criteriaBuilder14.lessThanOrEqualTo(
                            root.get("date"), getDateFormat(taskFilter.getDateTo())
                    ));
        }
        return specification;
    }
    private static Calendar getDateFormat(String stringDate) {
        LocalDate date = LocalDate.parse(stringDate, dateFormat);
        return GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
    }
}
