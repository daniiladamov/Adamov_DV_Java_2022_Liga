package homework.dto.security;

import homework.security.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAppDto {
    private Long id;

    private String password;

    private String firstName;

    private String lastName;

    private String surname;

    private String login;

    private RoleEnum role;

    private String uuid;
}
