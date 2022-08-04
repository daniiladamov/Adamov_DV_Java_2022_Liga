package homework.dto.request;

import homework.util.validator.UniqueLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserSaveDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String surname;

    @NotBlank
    @Size(min = 4)
    @UniqueLogin
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;
}
