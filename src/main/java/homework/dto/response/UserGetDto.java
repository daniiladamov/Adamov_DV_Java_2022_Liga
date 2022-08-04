package homework.dto.response;

import homework.security.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserGetDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String surname;

    private String login;

    private RoleEnum role;
}
