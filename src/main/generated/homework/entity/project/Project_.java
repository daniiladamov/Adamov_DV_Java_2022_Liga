package homework.entity.project;

import homework.entity.task.Task;
import homework.entity.user.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Project.class)
public abstract class Project_ {

	public static volatile SingularAttribute<Project, String> description;
	public static volatile SingularAttribute<Project, Long> id;
	public static volatile SingularAttribute<Project, String> title;
	public static volatile SetAttribute<Project, User> users;
	public static volatile SetAttribute<Project, Task> tasks;

	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String USERS = "users";
	public static final String TASKS = "tasks";

}

