package homework.entity.user;

import homework.entity.task.Task;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> name;
	public static volatile ListAttribute<User, Task> taskList;
	public static volatile SingularAttribute<User, Long> id;

	public static final String NAME = "name";
	public static final String TASK_LIST = "taskList";
	public static final String ID = "id";

}

