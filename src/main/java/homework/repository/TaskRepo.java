package homework.repository;

import homework.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepo extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
}
