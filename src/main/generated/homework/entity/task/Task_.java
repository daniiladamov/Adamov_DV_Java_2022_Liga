package homework.entity.task;

import homework.entity.EnumStatus;
import homework.entity.user.User;
import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Task.class)
public abstract class Task_ {

	public static volatile SingularAttribute<Task, Calendar> date;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, Long> id;
	public static volatile SingularAttribute<Task, String> title;
	public static volatile SingularAttribute<Task, User> user;
	public static volatile SingularAttribute<Task, EnumStatus> status;

	public static final String DATE = "date";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String USER = "user";
	public static final String STATUS = "status";

}

