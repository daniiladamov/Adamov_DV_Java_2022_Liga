package homework.util;

import homework.entity.project.Project;
import homework.entity.project.Project_;
import homework.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {

    public static Specification<Project> getUserProjects(User user){
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(user,root.get(Project_.users));
    }
}
