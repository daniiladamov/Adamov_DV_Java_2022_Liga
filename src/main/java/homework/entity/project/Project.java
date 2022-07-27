package homework.entity.project;

import com.sun.istack.NotNull;
import homework.entity.task.Task;
import homework.entity.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    @NotNull
    private String title;
    @Column(name = "description")
    @NotNull
    private String description;
    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name ="user_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    private Set<User> users=new HashSet<>();
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Task> tasks=new HashSet<>();
}
