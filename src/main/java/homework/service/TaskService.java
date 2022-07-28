package homework.service;

import com.sun.istack.NotNull;
import homework.util.CustomPage;
import homework.util.EnumStatus;
import homework.entity.task.Task;
import homework.entity.task.TaskFilter;
import homework.entity.task.Task_;
import homework.entity.user.User;
import homework.entity.user.User_;
import homework.repository.TaskRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
}
