package homework.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class UserSaveDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String surname;
    @NotNull
    private String login;
    @NotNull
    @Size(min = 8)
    private String password;
}
