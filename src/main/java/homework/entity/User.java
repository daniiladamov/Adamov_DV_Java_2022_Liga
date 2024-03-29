package homework.entity;

import homework.security.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users",uniqueConstraints= @UniqueConstraint(columnNames={"login"}))
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "surname")
    private String surname;

    @Size(min = 4)
    @Column(name = "login")
    @NotNull
    private String login;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Column(name = "jwt_uuid")
    private String uuid;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @Fetch(FetchMode.SUBSELECT)
    private List<Task> taskList = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @Fetch(FetchMode.SUBSELECT)
    private Set<Project> projects = new HashSet<>();

    @PrePersist
    public void addRole(){
        if (role==null)
            role=RoleEnum.ROLE_USER;
    }
}
