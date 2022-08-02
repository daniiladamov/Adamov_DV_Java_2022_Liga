package homework.dto;

import homework.entity.user.RoleEnum;
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
    private Long refreshDate;
}
