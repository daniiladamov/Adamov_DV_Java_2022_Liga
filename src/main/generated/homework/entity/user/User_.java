package homework.entity.user;

import homework.entity.Project;
import homework.entity.task.Task;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, RoleEnum> role;
	public static volatile SetAttribute<User, Project> projects;
	public static volatile SingularAttribute<User, String> surname;
	public static volatile ListAttribute<User, Task> taskList;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> login;
	public static volatile SingularAttribute<User, String> uuid;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";
	public static final String PROJECTS = "projects";
	public static final String SURNAME = "surname";
	public static final String TASK_LIST = "taskList";
	public static final String ID = "id";
	public static final String LOGIN = "login";
	public static final String UUID = "uuid";

}

