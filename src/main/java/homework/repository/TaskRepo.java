package homework.repository;

import homework.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepo extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
}
