package homework.repository;

import homework.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths ={"taskList"} )
    Optional<User> findById(Long aLong);
}
