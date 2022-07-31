package homework.entity;

import homework.entity.task.Task;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Comment.class)
public abstract class Comment_ {

	public static volatile SingularAttribute<Comment, Task> task;
	public static volatile SingularAttribute<Comment, Long> id;
	public static volatile SingularAttribute<Comment, String> content;

	public static final String TASK = "task";
	public static final String ID = "id";
	public static final String CONTENT = "content";

}

