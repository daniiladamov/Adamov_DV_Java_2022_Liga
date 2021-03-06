package homework.repository;

import homework.entity.task.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {
    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths ={"user"} )
    Optional<Task> findById(Long aLong);
}
