package homework.entity.comment;

import homework.entity.task.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    @NotNull
    private String content;
    @ManyToOne
    @JoinColumn(name="task_id",referencedColumnName = "id")
    @NotNull
    private Task task;
}
