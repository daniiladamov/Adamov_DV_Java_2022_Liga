package homework.util;

import homework.entity.Comment;
import homework.entity.Comment_;
import homework.entity.Project;
import homework.entity.Project_;
import homework.entity.task.Task;
import homework.entity.task.Task_;
import homework.entity.user.User;
import homework.entity.user.User_;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {

    public static Specification<Project> getUserProjects(User user){
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(user,root.get(Project_.users));
    }

    public static Specification<Task> getUserTasks(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Task_.user),user);
    }

    public static Specification<Task> getProjectTasks(Project project) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Task_.project),project);
    }

    public static Specification<User> getProjectUsers(Project project) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(project,root.get(User_.projects));
    }

    public static Specification<Comment> getTaskComments(Task task) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Comment_.task),task);
    }
}
