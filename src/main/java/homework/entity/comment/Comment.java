package homework.entity.comment;

import homework.entity.task.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JoinColumn(name="task_id",referencedColumnName = "id")
    private Task task;
}
