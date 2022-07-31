package homework.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAppDto {
    private Long id;
    private String password;
    private String firstName;
    private String lastName;
    private String surname;
    private String login;
    private RoleEnum role;
}
